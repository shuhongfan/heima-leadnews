package com.heima.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.admin.pojo.AdSensitive;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 敏感词mapper
 */
public interface AdSensitiveMapper extends BaseMapper<AdSensitive> {
    /**
     * 查询所有敏感词
     * @return
     */
    @Select("select sensitives from ad_sensitive")
    List<String> findAllSensitives();
}
