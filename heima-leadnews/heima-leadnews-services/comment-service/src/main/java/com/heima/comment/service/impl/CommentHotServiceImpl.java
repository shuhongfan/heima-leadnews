package com.heima.comment.service.impl;

import com.heima.comment.service.CommentHotService;
import com.heima.model.comment.dtos.CommentDTO;
import com.heima.model.comment.pojos.ApComment;
import com.heima.model.common.dtos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class CommentHotServiceImpl implements CommentHotService {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 处理热点评论
     * @param apComment 评论信息
     */
    // Async代表异步执行
    // taskExecutor是我们在配置中定义的线程池
    // Spring会使用线程池中的线程 异步执行此方法
    @Async("taskExecutor")
    public void hotCommentExecutor(ApComment apComment) {
        log.info("异步计算热点文章==================> 开始");
        // 1. 查询当前文章下的所有热点评论集合
        //     1.1 按照文章id   flag=1(热点文章)   点赞降序
        Query query = Query.query(Criteria.where("articleId").is(apComment.getArticleId())
                        .and("flag").is(1))
                .with(Sort.by(Sort.Direction.DESC, "likes"));
        List<ApComment> hotCommentList = mongoTemplate.find(query, ApComment.class);

        // 2. 如果 热评集合为空  或  热评数量小于5 直接将当前评论改为热评
        if (hotCommentList.size() < 5) {
            apComment.setFlag((short) 1);
            mongoTemplate.save(apComment);
            return;
        }

        // 3. 如果热评数量大于等于 5
        // 3.1  获取热评集合中 最后点赞数量最少的热评
        ApComment lastComment = hotCommentList.stream()
                .sorted(Comparator.comparing(ApComment::getLikes).reversed())
                .skip(hotCommentList.size() - 1)
                .findFirst()
                .get();

        // 3.2 和当前评论点赞数量做对比  谁的点赞数量多 改为热评
        if (lastComment.getLikes() < apComment.getLikes()) {
//            当前评论改为热评
            apComment.setFlag((short) 1);
            mongoTemplate.save(apComment);

//            最后的评论改为普通评论
            lastComment.setFlag((short) 0);
            mongoTemplate.save(lastComment);
        }

        log.info("异步计算热点文章==================> 结束");
    }

    /**
     * 根据 文章id 查询评论列表
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findByArticleId(CommentDTO dto) {
        //1 校验参数
        //   ========================需要变更处 start =============================
        //2 查询Mongo文章所有评论列表
        List<ApComment> apCommentList = null;
        if(dto.getIndex().intValue() == 1){ // 判断当前是否是第一页评论
            // 先查询热点评论集合  （最多5条） (条件: flag=1, 文章id, 点赞降序)
            // 新size = size - 热评数量
            // 查询第一页剩余普通评论 (条件: 文章id, flag=0, 时间降序, limit:新size)
            // 合并 热点评论  普通评论   热点list.addAll(普通list)
        }else {
            // 不是第一页直接查询普通评论
            // (条件: 文章id,flag=0,createdTime小于最小时间,时间降序,limit:size)
        }
        // ========================需要变更处 end =============================
        //3 封装查询结果
        //3.1 用户未登录 直接返回评论列表
        //3.2 用户登录，需要加载当前用户对评论点赞的列表
        return ResponseResult.okResult(apCommentList);
    }
}
