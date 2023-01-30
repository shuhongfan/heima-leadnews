package com.heima.model.behavior.dtos;
import lombok.Data;
@Data
public class ReadBehaviorDTO {
    // 设备ID
    Integer equipmentId;
    // 文章、动态、评论等ID
    Long articleId;
    /**
     * 阅读次数  
     */
    Short count;
    /**
     * 阅读时长（S)
     */
    Integer readDuration;
    /**
     * 阅读百分比
     */
    Short percentage;
    /**
     * 加载时间
     */
    Short loadDuration;
}