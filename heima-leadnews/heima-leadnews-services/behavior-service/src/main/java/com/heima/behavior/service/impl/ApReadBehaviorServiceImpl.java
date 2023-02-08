package com.heima.behavior.service.impl;
import java.util.Date;

import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.behavior.service.ApReadBehaviorService;
import com.heima.common.exception.CustException;
import com.heima.model.behavior.dtos.ReadBehaviorDTO;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import com.heima.model.behavior.pojos.ApReadBehavior;
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
public class ApReadBehaviorServiceImpl implements ApReadBehaviorService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ApBehaviorEntryService apBehaviorEntryService;

    /**
     * 记录阅读行为
     * @param dto
     * @return
     */
    @Override
    public ResponseResult readBehavior(ReadBehaviorDTO dto) {
//        校验参数 文章id必须传
//        根据登录用户id 或 设备id查询行为实体数据(阅读操作可不登录)
        ApUser user = AppThreadLocalUtils.getUser();

//        查询实体数据
        ApBehaviorEntry behaviorEntry = apBehaviorEntryService.findByUserIdOrEquipmentId(user == null ? null : user.getId(), dto.getEquipmentId());
        if (behaviorEntry == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "行为实体不存在");
        }

//        判断阅读行为是否存在
        Query query = Query.query(Criteria.where("entryId").is(behaviorEntry.getId()).and("articleId").is(dto.getArticleId()));
        ApReadBehavior apReadBehavior = mongoTemplate.findOne(query, ApReadBehavior.class);

//        存在 将阅读行为的count字段加1 并修改
        if (apReadBehavior != null) {
            apReadBehavior.setCount((short) (apReadBehavior.getCount() + 1));
            mongoTemplate.save(apReadBehavior);
        } else {
//        不存在 创建阅读行为 并初始化count字段值为 1
            apReadBehavior = new ApReadBehavior();
            apReadBehavior.setEntryId(behaviorEntry.getId());
            apReadBehavior.setArticleId(dto.getArticleId());
            apReadBehavior.setCount((short)0);
            apReadBehavior.setCreatedTime(new Date());
            apReadBehavior.setUpdatedTime(new Date());
            mongoTemplate.save(apReadBehavior);
        }

        return ResponseResult.okResult();
    }
}
