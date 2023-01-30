package com.heima.model.behavior.pojos;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.io.Serializable;
import java.util.Date;
@Data
@Document("ap_behavior_entry")
public class ApBehaviorEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 行为实体id 主键
     */
    private String id;
    /**
     * 实体类型
     0终端设备
     1用户
     */
    private Short type;
    /**
     * 关联id  type=0 设备id   type=1 用户id
     */
    private Integer refId;
    /**
     * 创建时间
     */
    private Date createdTime;
    public enum  Type{
        USER((short)1),EQUIPMENT((short)0);
        @Getter
        short code;
        Type(short code){
            this.code = code;
        }
    }
    public boolean isUser(){
        if(this.getType()!=null&&this.getType()== Type.USER.getCode()){
            return true;
        }
        return false;
    }
}