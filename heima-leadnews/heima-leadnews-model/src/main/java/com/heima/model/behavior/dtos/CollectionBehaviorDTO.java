package com.heima.model.behavior.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CollectionBehaviorDTO {
    // 设备ID
    Integer equipmentId;
    // 文章、动态ID
    @NotNull(message = "文章id不能为空")
    @JsonAlias("entryId") // 前端变量命名entryId 实际为articleId 因此起个别名
    Long articleId;

    /**
     * 收藏内容类型
     * 0文章
     * 1动态
     */
    Short type;
    /**
     * 操作类型
     * 0收藏
     * 1取消收藏
     */
    Short operation;
}