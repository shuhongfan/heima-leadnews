package com.heima.behavior.service.impl;

import com.heima.behavior.service.ApArticleBehaviorService;
import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.common.constants.user.UserRelationConstants;
import com.heima.common.exception.CustException;
import com.heima.model.behavior.dtos.ArticleBehaviorDTO;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApCollection;
import com.heima.model.behavior.pojos.ApLikesBehavior;
import com.heima.model.behavior.pojos.ApUnlikesBehavior;
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

@Service
public class ApArticleBehaviorServiceImpl implements ApArticleBehaviorService {

    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加载文章详情 数据回显
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loadArticleBehavior(ArticleBehaviorDTO dto) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("isfollow", false);
        map.put("islike", false);
        map.put("isunlike", false);
        map.put("iscollection", false);

//        1. 参数校验
//        判断用户是否登录
        ApUser apUser = AppThreadLocalUtils.getUser();
        if (apUser == null) {
//            未登录 返回默认数据
            return ResponseResult.okResult(map);
        }

//        已登录 查询实体
        ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(apUser.getId(), dto.getEquipmentId());
        if (behaviorEntry == null) {
            CustException.cust(AppHttpCodeEnum.DATA_EXIST, "实体不存在");
        }

        Query query = Query.query(Criteria.where("entryId").is(apUser.getId()).and("articleId").is(dto.getArticleId()));

//        查询文章喜欢行为
        ApLikesBehavior likesBehavior = mongoTemplate.findOne(query, ApLikesBehavior.class);
        if (likesBehavior != null) {
            map.remove("islike");
            map.put("islike", true);
        }

//        查询文章不喜欢行为
        ApUnlikesBehavior unlikesBehavior = mongoTemplate.findOne(query, ApUnlikesBehavior.class);
        if (unlikesBehavior != null) {
            map.remove("isunlike");
            map.put("isunlike", true);
        }

//        查询文章不喜欢行为
        ApCollection collection = mongoTemplate.findOne(query, ApCollection.class);
        if (collection != null) {
            map.remove("iscollection");
            map.put("iscollection", true);
        }

//        是否关注
        Double score = redisTemplate.opsForZSet().score(UserRelationConstants.FOLLOW_LIST + apUser.getId(), dto.getAuthorApUserId().toString());
        if (score != null) {
            map.remove("isfollow");
            map.put("isfollow", true);
        }

        return ResponseResult.okResult(map);
    }
}
