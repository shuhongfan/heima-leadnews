package com.heima.mongo.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document(collection = "ap_comment")
public class ApComment {
    @Id
    private String id;
    private Integer authorId;
    private String authorName;
    private Integer entryId;
    private Integer channelId;
    private Boolean type;
    private String content;
    private String image;
    private Integer likes;
    private Integer reply;
    private Byte flag;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String address;
    private Integer ord;
    private Date createdTime;
    private Date updatedTime;

}