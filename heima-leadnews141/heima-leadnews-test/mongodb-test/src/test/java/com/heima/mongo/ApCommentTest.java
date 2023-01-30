package com.heima.mongo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

/**
 * @author mrchen
 * @date 2022/5/3 10:05
 */
@SpringBootTest
public class ApCommentTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void saveDoc(){
        List<ApComment> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ApComment apComment = new ApComment();
            apComment.setContent("这是一条有味道的评论" + i);
            apComment.setLikes(new Random().nextInt(100));
            apComment.setReply(new Random().nextInt(100));
            apComment.setAddress("上海");
            apComment.setCreatedTime(new Date());
            list.add(apComment);
        }
        mongoTemplate.insertAll(list);
    }

    @Test
    public void updateDoc(){
        ApComment apComment = new ApComment();
        apComment.setId("62708ec088477910e0ebc83b");
        apComment.setContent("我修改了文章内容");
        apComment.setLikes(new Random().nextInt(100));
        apComment.setReply(new Random().nextInt(100));
        apComment.setAddress("上海");
        apComment.setCreatedTime(new Date());
        mongoTemplate.save(apComment);
    }

    @Test
    public void updateDocByMyself(){
        // 查询条件                           likes = 48   and   address = 上海
        Query query = Query.query(Criteria.where("likes").is(13).and("address").is("上海"));
        Update update = new Update();
        update.set("address","北京");
//        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, ApComment.class);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, ApComment.class);
        System.out.println(updateResult);
    }

    // 查询一个
    @Test
    public void testFindOne() throws Exception {
        //        ApComment comment = mongoTemplate.findById("60c2fe86c5a9e6651633a54a", ApComment.class);
        Query query = Query.query(Criteria.where("address").is("北京"));
        ApComment comment = mongoTemplate.findOne(query, ApComment.class);
        System.out.println(comment);
    }
    // 查询所有
    @Test
    public void testFindAll() throws Exception {
//        List<ApComment> commentList = mongoTemplate.findAll(ApComment.class);
//        for (ApComment apComment : commentList) {
//            System.out.println(apComment);
//        }
        mongoTemplate.remove(Query.query(Criteria.where("_id").is("62708ec088477910e0ebc83b")),ApComment.class);
    }

    @Test
    public void testFindByQuery() throws Exception {
        // 点赞 > 50    并且   城市 上海
        Query query = Query.query(Criteria.where("likes").gt(50).and("address").is("上海"));
        // 分页  方式一:
            //  跳过几条数据
//        query.skip(2);
            //  截取几条数据
//        query.limit(10);

        int page = 2;
        int size = 5;
               // spring分页参数  是从 0 计算
        query.with(PageRequest.of(page-1,size));
                // 排序
        query.with(Sort.by(Sort.Direction.DESC,"likes"));
        List<ApComment> list = mongoTemplate.find(query, ApComment.class);
        list.forEach(System.out::println);
    }
}
