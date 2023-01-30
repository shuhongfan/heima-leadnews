package com.heima.model.admin.dtos;
import com.heima.model.common.dtos.PageRequestDTO;
import lombok.Data;
@Data
public class SensitiveDTO extends PageRequestDTO {
    /**
     * 敏感词名称
     */
    private String name;
}