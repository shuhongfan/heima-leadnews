package com.heima.model.comment.pojos;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Date;
/**
 * APP评论回复信息
 */
@Data
@Document("ap_comment_repay")
public class ApCommentRepay {
    /**
     * id
     */
    @Id
    private String id;
    /**
     * 用户ID
     */
    private Integer authorId;
    /**
     * 用户昵称
     */
    private String authorName;
    /**
     * 评论id
     */
    private String commentId;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 点赞数
     */
    private Integer likes;
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
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

}