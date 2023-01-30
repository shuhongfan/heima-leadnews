package com.heima.admin.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.admin.pojo.AdSensitive;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdSensitiveMapper extends BaseMapper<AdSensitive> {

    @Select("select sensitives from ad_sensitive")
    List<String> findAllSensitives();
}