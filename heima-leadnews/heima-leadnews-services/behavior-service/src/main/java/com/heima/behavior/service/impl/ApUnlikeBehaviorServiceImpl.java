package com.heima.behavior.service.impl;

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

import java.util.Date;

@Service
public class ApUnlikeBehaviorServiceImpl implements ApUnlikeBehaviorService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;

    /**
     * 保存 或 取消 不喜欢
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult unlikeBehavior(UnLikesBehaviorDTO dto) {
//        1. 校验参数 文章id不能为空
//        用户必须登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN, "只有登录才能喜欢文章");
        }

//        2.查询行为实体
        ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(user.getId(), dto.getEquipmentId());
        if (behaviorEntry == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "行为实体不存在");
        }

//        3.获取 不喜欢行为是否存在
        Query query = Query.query(Criteria.where("entryId").is(behaviorEntry.getId()).and("articleId").is(dto.getArticleId()));
        ApUnlikesBehavior unlikesBehavior = mongoTemplate.findOne(query, ApUnlikesBehavior.class);
        if (unlikesBehavior != null && dto.getType().intValue() == 0) {
            CustException.cust(AppHttpCodeEnum.DATA_EXIST, "您已经设置不喜欢行为");
        }

//        4. 添加不喜欢行为
        if (dto.getType().intValue() == 0) {
            unlikesBehavior = new ApUnlikesBehavior();
            unlikesBehavior.setEntryId(behaviorEntry.getId());
            unlikesBehavior.setArticleId(dto.getArticleId());
            unlikesBehavior.setType((short) 0);
            unlikesBehavior.setCreatedTime(new Date());
            mongoTemplate.save(unlikesBehavior);
        } else {
//            5. 取消不喜欢行为
            mongoTemplate.remove(query, ApUnlikesBehavior.class);
        }

        return ResponseResult.okResult();
    }
}
