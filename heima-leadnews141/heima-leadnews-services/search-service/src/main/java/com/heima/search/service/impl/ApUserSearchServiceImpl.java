package com.heima.search.service.impl;

import com.heima.common.exception.CustException;
import com.heima.feigns.BehaviorFeign;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.HistorySearchDTO;
import com.heima.model.search.dtos.UserSearchDTO;
import com.heima.model.search.pojos.ApUserSearch;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import com.heima.search.service.ApUserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author mrchen
 * @date 2022/5/8 15:25
 */
@Service
public class ApUserSearchServiceImpl implements ApUserSearchService {
    @Autowired
    BehaviorFeign behaviorFeign;
    @Autowired
    MongoTemplate mongoTemplate;
    @Async("taskExecutor")
    @Override
    public void insert(UserSearchDTO dto) {
        String searchWords = dto.getSearchWords();
        // 1. 根据用户id 或 设备id 查询对应的行为实体
        ResponseResult<ApBehaviorEntry> result = behaviorFeign.findByUserIdOrEquipmentId(dto.getLoginUserId(), dto.getEquipmentId());
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        ApBehaviorEntry behaviorEntry = result.getData();
        // 2. 根据行为实体id 及  关键词查询是否存在
        Query query = Query.query(Criteria.where("entryId").is(behaviorEntry.getId()).and("keyword").is(searchWords));
        ApUserSearch userSearch = mongoTemplate.findOne(query, ApUserSearch.class);
        // 3. 如果存在该历史记录，修改创建时间
        if (userSearch!=null) {
            userSearch.setCreatedTime(new Date());
            mongoTemplate.save(userSearch);
            return;
        }
        // 4. 如果不存在  则 新增记录
        userSearch = new ApUserSearch();
        userSearch.setEntryId(behaviorEntry.getId());
        userSearch.setKeyword(searchWords);
        userSearch.setCreatedTime(new Date());
        mongoTemplate.insert(userSearch);
    }

    @Override
    public ResponseResult findUserSearch(UserSearchDTO dto) {
        // 1. 查询用户的登陆信息
        ApUser user = AppThreadLocalUtils.getUser();
        // 2. 根据用户id 或 设备id 查询行为实体
        ResponseResult<ApBehaviorEntry> result = behaviorFeign.findByUserIdOrEquipmentId(user == null?null:user.getId(), dto.getEquipmentId());
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        ApBehaviorEntry behaviorEntry = result.getData();
        // 3. 根据行为实体id查询  历史记录
        List<ApUserSearch> apUserSearches = mongoTemplate.find(Query.query(Criteria.where("entryId").is(behaviorEntry.getId()))
                        .with(Sort.by(Sort.Direction.DESC, "createdTime"))
                        .limit(10)
                , ApUserSearch.class);
        // 默认查询10条历史记录，并且按照时间降序排序
        return ResponseResult.okResult(apUserSearches);
    }

    @Override
    public ResponseResult delUserSearch(HistorySearchDTO historySearchDto) {
        if(historySearchDto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser user = AppThreadLocalUtils.getUser();
        ResponseResult<ApBehaviorEntry> behaviorEntryResult = behaviorFeign.findByUserIdOrEquipmentId(user == null ? null : user.getId(), historySearchDto.getEquipmentId());
        if(!behaviorEntryResult.checkCode()){
            return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR,"远程调用行为服务失败");
        }
        ApBehaviorEntry behaviorEntry = behaviorEntryResult.getData();
        if(behaviorEntry == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"行为实体数据不存在");
        }
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(historySearchDto.getId()).and("entryId").is(behaviorEntry.getId())),ApUserSearch.class);
        return ResponseResult.okResult();
    }
}
