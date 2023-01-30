package com.heima.comment.service.impl;
import com.heima.comment.service.CommentHotService;
import com.heima.model.comment.pojos.ApComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@Slf4j
public class CommentHotServiceImpl implements CommentHotService {
    @Autowired
    MongoTemplate mongoTemplate;
    /**
     * 处理热点评论
     * @param apComment 评论信息
     */
    @Async("taskExecutor")
    @Override
    public void hotCommentExecutor(ApComment apComment) {
        log.info("异步计算热点文章==================> 开始");
        // 1. 按照文章id   flag=1(热点文章)   点赞降序
        Query query = Query.query(Criteria.where("articleId")
                .is(apComment.getArticleId()).and("flag").is(1))
                .with(Sort.by(Sort.Direction.DESC,"likes"));
        List<ApComment> hotCommentList = mongoTemplate.find(query, ApComment.class);
        // 2. 如果 热评集合为空  或  数量小于5 直接将当前评论改为热评
        if(hotCommentList == null || hotCommentList.size() < 5){
            apComment.setFlag((short)1);
            mongoTemplate.save(apComment);
            return;
        }
        // 3. 获取热评集合中 最后点赞数量最少的热评
        ApComment lastHotComment = hotCommentList.get(hotCommentList.size() - 1);
        // 4. 和当前评论点赞数量做对比  谁的点赞数量多 改为热评
        if(apComment.getLikes() > lastHotComment.getLikes()){
            // 当前评论改为热评
            apComment.setFlag((short)1);
            mongoTemplate.save(apComment);

            // 最后的评论改为普通评论
            lastHotComment.setFlag((short)0);
            mongoTemplate.save(lastHotComment);
        }
        log.info("异步计算热点文章==================> 结束");
    }
}
