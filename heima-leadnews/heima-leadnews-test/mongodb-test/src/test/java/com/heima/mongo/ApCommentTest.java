package com.heima.mongo;

import com.heima.mongo.pojo.ApComment;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class ApCommentTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void saveDoc() {
        ApComment apComment = new ApComment();
        apComment.setContent("这一条有味道的评论");
        apComment.setLikes(10);
        apComment.setReply(20);
        apComment.setAddress("北京");
        apComment.setCreatedTime(new Date());
        mongoTemplate.insert(apComment);
    }

    @Test
    public void saveDocList() {
        ArrayList<ApComment> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ApComment apComment = new ApComment();
            apComment.setContent("这一条有味道的评论"+i);
            apComment.setLikes(new Random().nextInt(100));
            apComment.setReply(new Random().nextInt(100));
            apComment.setAddress("北京");
            apComment.setCreatedTime(new Date());
            list.add(apComment);
        }
        mongoTemplate.insertAll(list);
    }

    @Test
    public void updateDoc() {
        ApComment apComment = new ApComment();
        apComment.setId("63e34aaa9a03712462370a7c");
        apComment.setContent("vfddfb");
        apComment.setLikes(10);
        apComment.setReply(20);
        apComment.setAddress("北京");
        apComment.setCreatedTime(new Date());
        mongoTemplate.save(apComment);
    }

    @Test
    public void updateDocBy() {
//        查询条件
        Query query = Query.query(Criteria.where("likes").is(48).and("address").is("上海"));

        Update update = new Update();
        update.set("address", "北京");

        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ApComment.class);
        System.out.println(updateResult);
    }

    @Test
    public void testFindOne() {
        Query query = Query.query(Criteria.where("address").is("北京"));
        ApComment one = mongoTemplate.findOne(query, ApComment.class);
        System.out.println(one);
    }

    @Test
    public void testFindAll() {
        List<ApComment> list = mongoTemplate.findAll(ApComment.class);
        System.out.println(list);
    }

    @Test
    public void testFindByQuery() {
//        点赞>50 并且 城市在北京
        Query query = Query.query(Criteria.where("likes").gt(50).and("address").is("北京"));

//        分页
//        跳过几条数据
//        query.skip(2);

//        截取几条数据
//        query.limit(3);

//        Spring分页参数 从0开始计算
        int page = 1;
        int size = 5;
        query.with(PageRequest.of(page - 1, size));

//        排序
        query.with(Sort.by(Sort.Direction.DESC, "likes"));


        List<ApComment> apComments = mongoTemplate.find(query, ApComment.class);
        System.out.println(apComments);
    }

    @Test
    public void testRemove() {
        Query query = Query.query(Criteria.where("_id").is("63e34aaa9a03712462370a7c"));

        mongoTemplate.remove(query, ApComment.class);
    }
}
