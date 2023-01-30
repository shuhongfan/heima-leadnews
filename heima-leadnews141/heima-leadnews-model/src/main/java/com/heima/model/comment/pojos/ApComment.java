package com.heima.model.comment.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * APP评论信息
 */
@Data
@Document("ap_comment")
public class ApComment {
    /**
     * id
     */
    private String id;
    /**
     * 用户ID  评论的用户ID（当前登录人ID  apUserId）
     */
    private Integer authorId;
    /**
     * 用户昵称
     */
    private String authorName;

    /**
     * 文章id或动态id
     */
    private Long articleId;
      /**
     * 评论内容类型
     * 0 文章
     * 1 动态
     */
    private Short type;
    /**
     * 频道ID
     */
    private Integer channelId;
    /**
     * 评论内容 不能大于140个字
     */
    private String content;
    /**
     * 登录人头像
     */
    private String image;
    /**
     * 点赞数
     */
    private Integer likes;
    /**
     * 回复数
     */
    private Integer reply;
    /**
     * 文章标记
     * 0 普通评论
     * 1 热点评论
     * 2 推荐评论
     * 3 置顶评论
     * 4 精品评论
     * 5 大V 评论
     */
    private Short flag;
    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 维度
     */
    private BigDecimal latitude;

    /**
     * 地理位置
     */
    private String address;

    /**
     * 评论排列序号
     */
    private Integer ord;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

}