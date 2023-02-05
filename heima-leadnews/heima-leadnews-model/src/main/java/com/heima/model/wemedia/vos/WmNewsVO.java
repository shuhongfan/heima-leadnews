package com.heima.model.wemedia.vos;

import com.heima.model.wemedia.pojos.WmNews;
import lombok.Data;

@Data
public class WmNewsVO  extends WmNews {
    /**
     * 作者名称
     */
    private String authorName;
}