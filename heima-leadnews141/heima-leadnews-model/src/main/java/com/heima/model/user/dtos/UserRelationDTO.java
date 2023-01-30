package com.heima.model.user.dtos;
import lombok.Data;
@Data
public class UserRelationDTO {
    // 文章作者ID
    Integer authorId; // 未使用
    // 作者对应的apuserid
    Integer authorApUserId;
    // 文章id
    Long articleId;
    /**
     * 操作方式
     * 0  关注
     * 1  取消
     */
    Short operation;
}