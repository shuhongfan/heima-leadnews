package com.heima.model.mess.app;
import lombok.Data;
@Data
public class NewBehaviorDTO {
    /**
     * 修改文章的字段类型
      */
    private BehaviorType type;
    /**
     * 文章ID
     */
    private Long articleId;
  
    /**
     * 次数 +1  -1
     */
    private Integer add;
  
    public enum BehaviorType{ // 行为类型
        COLLECTION,COMMENT,LIKES,VIEWS;
    }
}