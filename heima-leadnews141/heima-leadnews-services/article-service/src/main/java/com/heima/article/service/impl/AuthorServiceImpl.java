package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.mapper.AuthorMapper;
import com.heima.article.service.AuthorService;
import com.heima.model.article.pojos.ApAuthor;
import org.springframework.stereotype.Service;

/**
 * @author mrchen
 * @date 2022/4/22 15:41
 */
@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper,ApAuthor> implements AuthorService {
}
