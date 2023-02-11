package com.heima.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.behavior.filter.AppTokenFilter;
import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.behavior.service.ApLikesBehaviorService;
import com.heima.common.constants.article.HotArticleConstants;
import com.heima.common.exception.CustException;
import com.heima.model.behavior.dtos.LikesBehaviorDTO;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApLikesBehavior;
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

import java.util.Date;

@Service
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 点赞或取消点赞
     * @param dto
     * @return
     */
    @Override
    public ResponseResult like(LikesBehaviorDTO dto) {
    // 1. 校验参数
    // 点赞需要登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN,"点赞需要登录");
        }
        // 校验文章id不能为空 使用注解校验
    // 校验点赞方式 只能是0 或 1 使用注解校验

    // 2. 根据当前登录用户id查询行为实体对象
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(user.getId(), dto.getEquipmentId());
        if (apBehaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "获取行为实体信息失败");
        }

        // 3. 如果是点赞操作 判断是否已经点过赞
        Query query = Query.query(Criteria.where("entryId").is(apBehaviorEntry.getId()).and("articleId").is(dto.getArticleId()));
        ApLikesBehavior likesBehavior = mongoTemplate.findOne(query, ApLikesBehavior.class);
        if (dto.getOperation().intValue() == 0 && likesBehavior != null) {
            CustException.cust(AppHttpCodeEnum.DATA_EXIST, "请勿重复点赞");
        }

        // 4. 没有点过赞则 像mongo点赞集合中 添加点赞数据
        if (dto.getOperation().intValue() == 0) {
            ApLikesBehavior apLikesBehavior = new ApLikesBehavior();
            apLikesBehavior.setCreatedTime(new Date());
            apLikesBehavior.setArticleId(dto.getArticleId());
            apLikesBehavior.setEntryId(apBehaviorEntry.getId());
            apLikesBehavior.setType((short) 0);
            apLikesBehavior.setOperation((short) 0);
            mongoTemplate.save(apLikesBehavior);
        } else {
            // 5. 如果是取消点赞操作 在mongo点赞集合中 删除对应点赞数据
            mongoTemplate.remove(query, ApLikesBehavior.class);
        }

//        发送行为消息
        NewBehaviorDTO newBehaviorDTO = new NewBehaviorDTO();
        newBehaviorDTO.setType(NewBehaviorDTO.BehaviorType.LIKES);
        newBehaviorDTO.setArticleId(dto.getArticleId());
        newBehaviorDTO.setAdd(dto.getOperation().intValue() == 0 ? 1 : -1);

        rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE, JSON.toJSON(newBehaviorDTO));
        log.info("发送成功 文章点赞行为消息，消息内容：{}", newBehaviorDTO);
        return ResponseResult.okResult();
    }
}
