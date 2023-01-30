package com.heima.behavior.service.impl;
import com.heima.model.mess.app.NewBehaviorDTO.BehaviorType;
import com.alibaba.fastjson.JSON;
import java.util.Date;
import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.behavior.service.ApLikesBehaviorService;
import com.heima.common.exception.CustException;
import com.heima.model.behavior.dtos.LikesBehaviorDTO;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApLikesBehavior;
import com.heima.model.common.constants.article.HotArticleConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mess.app.NewBehaviorDTO;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {
    @Autowired
    ApBehaviorEntryService apBehaviorEntryService;
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;


    @Override
    public ResponseResult like(LikesBehaviorDTO dto) {
        // 1. 校验参数
        ApUser user = AppThreadLocalUtils.getUser();
        if(user == null){
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 2. 查询行为实体
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(user.getId(), dto.getEquipmentId());
        if (apBehaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"获取行为实体信息失败");
        }
        // 3. 查询是否有该行为实体的点赞记录 如果是点赞并且有记录 提示请勿重复点赞
        Query query = Query.query(Criteria.where("entryId").is(apBehaviorEntry.getId()).and("articleId").is(dto.getArticleId()));
        ApLikesBehavior likesBehavior = mongoTemplate.findOne(query, ApLikesBehavior.class);
        if(dto.getOperation().intValue() == 0 && likesBehavior!=null){
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"请勿重复点赞");
        }
        // 4. 点赞或取消点赞
        if(dto.getOperation().intValue() == 0){
            // 点赞   添加点赞数据
            likesBehavior = new ApLikesBehavior();
            likesBehavior.setEntryId(apBehaviorEntry.getId());
            likesBehavior.setArticleId(dto.getArticleId());
            likesBehavior.setType((short)0);
            likesBehavior.setOperation((short)0);
            likesBehavior.setCreatedTime(new Date());
            mongoTemplate.save(likesBehavior);
        }else {
            // 取消点赞  删除点赞数据
            mongoTemplate.remove(query,ApLikesBehavior.class);
        }
        // 发送新行为消息
        NewBehaviorDTO newBehavior = new NewBehaviorDTO();
        newBehavior.setType(BehaviorType.LIKES);
        newBehavior.setArticleId(dto.getArticleId());
        newBehavior.setAdd(dto.getOperation().intValue()==0?1:-1);
        rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE,JSON.toJSONString(newBehavior));
        log.info("成功发送 文章点赞行为消息 , 消息内容: {}",newBehavior);
        return ResponseResult.okResult();
    }
}
