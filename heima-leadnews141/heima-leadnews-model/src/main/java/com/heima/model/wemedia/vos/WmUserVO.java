package com.heima.model.wemedia.vos;

import lombok.Data;

import java.util.Date;

@Data
public class WmUserVO {
    private Integer id;
    private String name;
    private String nickname;
    private String image;
    private String email;
    private Date loginTime;
    private Date createdTime;
}