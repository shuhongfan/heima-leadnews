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
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ApUserSearchServiceImpl implements ApUserSearchService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BehaviorFeign behaviorFeign;

    /**
     * 保存用户搜索历史记录
     * @param dto
     */
    @Async("taskExecutor")
    @Override
    public void insert(UserSearchDTO dto) {
        String searchWords = dto.getSearchWords();

//        1. 根据用户id 或 设备id 查询对应的行为实体
        ResponseResult<ApBehaviorEntry> result = behaviorFeign.findByUserIdOrEquipmentId(dto.getEntryId(), dto.getEquipmentId());
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }
        ApBehaviorEntry behaviorEntry = result.getData();

//        2. 根据行为实体id 及 关键词查询是否存在
        Query query = Query.query(Criteria.where("entryId").is(behaviorEntry.getId()).and("keyword").is(searchWords));
        ApUserSearch userSearch = mongoTemplate.findOne(query, ApUserSearch.class);

//        3. 如果存在该历史记录，修改创建时间
        if (userSearch != null) {
            userSearch.setCreatedTime(new Date());
            mongoTemplate.save(userSearch);
            return;
        }

//        4. 如果不存在 则新增记录
        userSearch = new ApUserSearch();
        userSearch.setEntryId(behaviorEntry.getId());
        userSearch.setKeyword(searchWords);
        userSearch.setCreatedTime(new Date());
        mongoTemplate.insert(userSearch);
    }

    /**
     * 查询用户搜索历史记录  显示10条
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findUserSearch(UserSearchDTO dto) {
//        1. 查询用户的登录信息
        ApUser user = AppThreadLocalUtils.getUser();

//        2. 根据用户id或设备id 查询行为数据
        ResponseResult<ApBehaviorEntry> result = behaviorFeign.findByUserIdOrEquipmentId(user == null ? null : user.getId(), dto.getEquipmentId());
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }
        ApBehaviorEntry behaviorEntry = result.getData();

//        3. 根据行为实体id查询 历史记录
//        默认查询10条历史记录，并且按照时间降序排序
        Query query = Query.query(Criteria.where("entryId").is(behaviorEntry.getId()))
                .with(Sort.by(Sort.Direction.DESC, "createdTime"))
                .limit(10);
        List<ApUserSearch> apUserSearchList = mongoTemplate.find(query, ApUserSearch.class);

        return ResponseResult.okResult(apUserSearchList);
    }

    /**
     删除搜索历史
     @param historySearchDto
     @return
     */
    @Override
    public ResponseResult delUserSearch(HistorySearchDTO historySearchDto) {
//        1. 校验参数
        if (historySearchDto.getId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

//       2. 判断实体数据是否存在
        ApUser user = AppThreadLocalUtils.getUser();
        ResponseResult<ApBehaviorEntry> result = behaviorFeign.findByUserIdOrEquipmentId(user == null ? null : user.getId(), historySearchDto.getEquipmentId());
        if (!result.checkCode()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR, "远程调用行为服务失败");
        }

        ApBehaviorEntry behaviorEntry = result.getData();
        if (behaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "行为实体数据不存在");
        }

//        3. 删除实体数据
        Query query = Query.query(Criteria.where("_id").is(historySearchDto.getId()).and("entryId").is(behaviorEntry.getId()));
        mongoTemplate.remove(query, ApUserSearch.class);

        return ResponseResult.okResult();
    }

}
