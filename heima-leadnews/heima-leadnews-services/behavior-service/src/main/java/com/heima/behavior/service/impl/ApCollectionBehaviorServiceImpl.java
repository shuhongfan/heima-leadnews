package com.heima.behavior.service.impl;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.behavior.service.ApCollectionBehaviorService;
import com.heima.common.constants.article.HotArticleConstants;
import com.heima.common.exception.CustException;
import com.heima.model.behavior.dtos.CollectionBehaviorDTO;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApCollection;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.message.app.NewBehaviorDTO;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Currency;

@Service
@Slf4j
public class ApCollectionBehaviorServiceImpl implements ApCollectionBehaviorService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 收藏 取消收藏
     * @param dto
     * @return
     */
    @Override
    public ResponseResult collectBehavior(CollectionBehaviorDTO dto) {
//        1. 参数校验 用户必须登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }

//        2. 获取实体
        ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(user.getId(), dto.getEquipmentId());
        if (behaviorEntry == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "实体不存在");
        }

//        3. 判断用户是否收藏
        Query query = Query.query(Criteria.where("entryId").is(behaviorEntry.getId()).and("articleId").is(dto.getArticleId()));
        ApCollection collection = mongoTemplate.findOne(query, ApCollection.class);
        if (collection != null && dto.getType().intValue() == 0) {
            CustException.cust(AppHttpCodeEnum.DATA_EXIST, "用户以及收藏");
        }

//        4. 创建收藏
        if (dto.getType().intValue() == 0) {
            collection = new ApCollection();
            collection.setEntryId(behaviorEntry.getId());
            collection.setArticleId(dto.getArticleId());
            collection.setType((short) 0);
            collection.setCollectionTime(new Date());
            mongoTemplate.save(collection);
        } else {
//            5. 删除收藏
            mongoTemplate.remove(query, ApCollection.class);
        }

//        发送行为消息
        NewBehaviorDTO newBehaviorDTO = new NewBehaviorDTO();
        newBehaviorDTO.setType(NewBehaviorDTO.BehaviorType.COLLECTION);
        newBehaviorDTO.setArticleId(dto.getArticleId());
        newBehaviorDTO.setAdd(dto.getOperation().intValue() == 0 ? 1 : -1);

        rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE, JSON.toJSONString(newBehaviorDTO));
        log.info("发送成功 文章收藏行为消息，消息内容：{}", newBehaviorDTO);

        return ResponseResult.okResult();
    }
}
