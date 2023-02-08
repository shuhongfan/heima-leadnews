package com.heima.model.behavior.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("ap_read_behavior")
public class ApReadBehavior implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 用户ID
     */
    private String entryId;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 阅读次数
     */
    private Short count;
    /**
     * 登录时间
     */
    private Date createdTime;
    /**
     * 更新时间
     */
    private Date updatedTime;
}