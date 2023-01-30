package com.heima.behavior.service.impl;
import java.util.Date;
import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.behavior.service.ApCollectionBehaviorService;
import com.heima.common.exception.CustException;
import com.heima.model.behavior.dtos.CollectionBehaviorDTO;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApCollection;
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
public class ApCollectionBehaviorServiceImpl implements ApCollectionBehaviorService {
    @Autowired
    ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public ResponseResult collectBehavior(CollectionBehaviorDTO dto) {
        // 1. 校验参数
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 2. 查询行为实体
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(user.getId(), dto.getEquipmentId());
        if (apBehaviorEntry == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"行为实体数据不存在");
        }
        Query query = Query.query(Criteria.where("entryId").is(apBehaviorEntry.getId()).and("articleId").is(dto.getArticleId()));
        // 3. 如果是收藏操作，查询是否已经收藏
        if(dto.getOperation().intValue() == 0){
            ApCollection apCollection = mongoTemplate.findOne(query, ApCollection.class);
            if(apCollection!=null){
                CustException.cust(AppHttpCodeEnum.DATA_EXIST,"您已收藏该文章");
            }
            apCollection = new ApCollection();
            apCollection.setEntryId(apBehaviorEntry.getId());
            apCollection.setArticleId(dto.getArticleId());
            apCollection.setType((short)0);
            apCollection.setCollectionTime(new Date());
            mongoTemplate.save(apCollection);
        }else {
            mongoTemplate.remove(query,ApCollection.class);
        }
        return ResponseResult.okResult();
    }
}