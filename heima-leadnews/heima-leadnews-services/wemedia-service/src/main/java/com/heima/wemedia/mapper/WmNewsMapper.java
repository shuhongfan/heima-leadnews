package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wemedia.dtos.NewsAuthDTO;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.vos.WmNewsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WmNewsMapper extends BaseMapper<WmNews> {
    /**
     * 查询文章列表
     * @param dto
     * @return
     */
    List<WmNewsVO> findListAndPage(@Param("dto") NewsAuthDTO dto);

    /**
     * 查询文章数量
     * @param dto
     * @return
     */
    long findListCount(@Param("dto") NewsAuthDTO dto);
}
