package com.heima.model.article.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP文章作者信息表
 * </p>
 *
 * @author itheima
 */
@Data
@TableName("ap_author")
public class ApAuthor implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 作者名称
     */
    @TableField("name")
    private String name;
    /**
     * 0 爬取数据
     1 签约合作商
     2 平台自媒体人
     */
    @TableField("type")
    private Integer type;
    /**
     * 社交账号ID
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 自媒体账号
     */
    @TableField("wm_user_id")
    private Integer wmUserId;
}