package com.heima.behavior.service.impl;

import com.heima.behavior.service.ApBehaviorEntryService;
import com.heima.model.behavior.pojos.ApBehaviorEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class ApBehaviorEntryServiceImpl implements ApBehaviorEntryService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public synchronized ApBehaviorEntry findByUserIdOrEquipmentId(Integer userId, Integer equipmentId) {
        //1. 判断userId是否为空  不为空 使用userId查询  如果不存在基于userId创建实体数据
        if(userId != null){
            ApBehaviorEntry behaviorEntry = mongoTemplate.findOne(Query.query(Criteria.where("refId").is(userId)
                    .and("type").is(ApBehaviorEntry.Type.USER.getCode())), ApBehaviorEntry.class);
            if(behaviorEntry == null){
                behaviorEntry = new ApBehaviorEntry();
                behaviorEntry.setType(ApBehaviorEntry.Type.USER.getCode());
                behaviorEntry.setRefId(userId);
                behaviorEntry.setCreatedTime(new Date());
                mongoTemplate.save(behaviorEntry);
            }
            return behaviorEntry;
        }
        //2. 判断设备id是否为空   不为空 使用设备id查询  如果不存在基于设备id创建实体数据
        if(equipmentId != null){
            ApBehaviorEntry behaviorEntry = mongoTemplate.findOne(Query.query(Criteria.where("refId").is(equipmentId)
                    .and("type").is(ApBehaviorEntry.Type.EQUIPMENT.getCode())), ApBehaviorEntry.class);
            if(behaviorEntry == null){
                behaviorEntry = new ApBehaviorEntry();
                behaviorEntry.setType(ApBehaviorEntry.Type.EQUIPMENT.getCode());
                behaviorEntry.setRefId(equipmentId);
                behaviorEntry.setCreatedTime(new Date());
                mongoTemplate.save(behaviorEntry);
            }
            return behaviorEntry;
        }
        return null;
    }
}