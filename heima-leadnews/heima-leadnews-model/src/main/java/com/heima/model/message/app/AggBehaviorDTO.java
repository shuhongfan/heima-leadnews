package com.heima.model.message.app;

import lombok.Data;

/**
 * 行为聚合数据
 * @author mrchen
 */
@Data
public class AggBehaviorDTO {
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 阅读
     */
    private long view;
    /**
     * 收藏
     */
    private long collect;
    /**
     * 评论
     */
    private long comment;
    /**
     * 点赞
     */
    private long like;
}
