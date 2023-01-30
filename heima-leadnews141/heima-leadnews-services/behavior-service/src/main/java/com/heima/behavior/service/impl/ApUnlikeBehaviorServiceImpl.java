package com.heima.behavior.service.impl;
import java.util.Date;
import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.behavior.service.ApUnlikeBehaviorService;
import com.heima.common.exception.CustException;
import com.heima.model.behavior.dtos.UnLikesBehaviorDTO;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApUnlikesBehavior;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
@Service
public class ApUnlikeBehaviorServiceImpl implements ApUnlikeBehaviorService {

    @Autowired
    ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResponseResult unlikeBehavior(UnLikesBehaviorDTO dto) {
        //1. 校验参数
        ApUser user = AppThreadLocalUtils.getUser();
        if(user == null){
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        //2. 查询行为实体
        ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(user.getId(), dto.getEquipmentId());
        if(behaviorEntry == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"获取行为实体数据失败");
        }
        //3. 查询不喜欢行为是否已经存在
        Query query = Query.query(Criteria.where("entryId").is(behaviorEntry.getId()).and("articleId").is(dto.getArticleId()));
        ApUnlikesBehavior unlikesBehavior = mongoTemplate.findOne(query, ApUnlikesBehavior.class);
        if(unlikesBehavior != null && dto.getType().intValue() == 0 ){
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"您已设置不喜欢");
        }
        //4. 添加不喜欢行为
        if(dto.getType().intValue() == 0){
            unlikesBehavior = new ApUnlikesBehavior();
            unlikesBehavior.setEntryId(behaviorEntry.getId());
            unlikesBehavior.setArticleId(dto.getArticleId());
            unlikesBehavior.setType((short)0);
            unlikesBehavior.setCreatedTime(new Date());
            mongoTemplate.save(unlikesBehavior);
        }else {
            //5. 取消不喜欢行为
            mongoTemplate.remove(query,ApUnlikesBehavior.class);
        }
        return ResponseResult.okResult();
    }
}
