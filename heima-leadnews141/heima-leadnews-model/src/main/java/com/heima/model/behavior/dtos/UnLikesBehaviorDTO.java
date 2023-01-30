package com.heima.model.behavior.dtos;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class UnLikesBehaviorDTO {
    // 设备ID
    @NotNull(message = "设备id不能为空")
    Integer equipmentId;
    // 文章ID
    @NotNull(message = "文章id不能为空")
    Long articleId;
    /**
     * 不喜欢操作方式
     * 0 不喜欢
     * 1 取消不喜欢
     */
    @Range(min = 0,max = 1,message = "操作类型错误")
    Short type;

}