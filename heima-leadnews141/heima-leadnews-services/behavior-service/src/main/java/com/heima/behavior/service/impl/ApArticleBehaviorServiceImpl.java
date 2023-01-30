package com.heima.behavior.service.impl;
import com.heima.behavior.service.ApArticleBehaviorService;
import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.common.exception.CustException;
import com.heima.model.behavior.dtos.ArticleBehaviorDTO;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApCollection;
import com.heima.model.behavior.pojos.ApLikesBehavior;
import com.heima.model.behavior.pojos.ApUnlikesBehavior;
import com.heima.model.common.constants.user.UserRelationConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
@Service
public class ApArticleBehaviorServiceImpl implements ApArticleBehaviorService {
    @Autowired
    ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public ResponseResult loadArticleBehavior(ArticleBehaviorDTO dto) {
        //1.检查参数
        //{ "isfollow": true, "islike": true,"isunlike": false,"iscollection": true }
        boolean isfollow = false, islike = false, isunlike = false, iscollection = false;
        ApUser user = AppThreadLocalUtils.getUser();
        if(user != null){
            ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(user.getId(), dto.getEquipmentId());
            if (behaviorEntry == null) {
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
            Query query = Query.query(Criteria.where("entryId").is(behaviorEntry.getId()).and("articleId").is(dto.getArticleId()));
            //喜欢行为
            ApLikesBehavior likesBehavior = mongoTemplate.findOne(query, ApLikesBehavior.class);
            if (likesBehavior != null) {
                islike = true;
            }
            //不喜欢行为
            ApUnlikesBehavior unlikesBehavior = mongoTemplate.findOne(query, ApUnlikesBehavior.class);
            if (unlikesBehavior != null) {
                isunlike = true;
            }
            //是否收藏
            ApCollection apCollection = mongoTemplate.findOne(query, ApCollection.class);
            if (apCollection != null) {
                iscollection = true;
            }
            //是否关注
            Double score = redisTemplate.opsForZSet().score(UserRelationConstants.FOLLOW_LIST + user.getId(),dto.getAuthorApUserId().toString());
            if(score != null){
                isfollow = true;
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isfollow", isfollow);
        resultMap.put("islike", islike);
        resultMap.put("isunlike", isunlike);
        resultMap.put("iscollection", iscollection);
        return ResponseResult.okResult(resultMap);
    }
}