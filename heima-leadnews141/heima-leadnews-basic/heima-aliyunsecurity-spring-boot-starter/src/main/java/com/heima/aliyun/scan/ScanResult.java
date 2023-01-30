package com.heima.aliyun.scan;

import lombok.Data;

/**
 * @author mrchen
 * @date 2022/4/27 20:55
 */
@Data
public class ScanResult {
    private boolean success;
    /**
     * 建议:
     */
    private String suggestion;
    /**
     * 场景:
     */
    private String scene;
    /**
     * 违规标签:
     */
    private String label;
    /**
     * 过滤后的字符
     */
    private String filteredContent;
    /**
     * 违规原因:
     */
    private String reason;

}
