package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.service.ApArticleContentService;
import com.heima.model.article.pojos.ApArticleContent;
import org.springframework.stereotype.Service;

@Service
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper,ApArticleContent> implements ApArticleContentService {

}