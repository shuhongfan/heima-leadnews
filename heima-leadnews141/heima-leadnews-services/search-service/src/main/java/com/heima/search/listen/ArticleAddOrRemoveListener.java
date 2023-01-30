package com.heima.search.listen;

import com.heima.common.exception.CustException;
import com.heima.feigns.ArticleFeign;
import com.heima.model.article.vos.SearchArticleVO;
import com.heima.model.common.constants.message.NewsUpOrDownConstants;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.search.service.ArticleSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mrchen
 * @date 2022/5/8 14:48
 */
@Component
@Slf4j
public class ArticleAddOrRemoveListener {
    @Autowired
    ArticleSearchService articleSearchService;
    @Autowired
    ArticleFeign articleFeign;
    @RabbitListener(queues = NewsUpOrDownConstants.NEWS_UP_FOR_ES_QUEUE)
    public void listenNewsUpMsg(String articleId){
        log.info("接收到文章上架消息， 消息内容: {}",articleId);
        // 根据articleId查询文章信息，将文章信息 添加到es索引库中
        // 远程查询article信息
        ResponseResult<SearchArticleVO> result = articleFeign.findArticle(Long.valueOf(articleId));
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"未查询到对应的文章数据");
            log.error("未查询到对应的文章信息,文章id:{}",articleId);
        }
        articleSearchService.saveArticle(result.getData());
    }

    @RabbitListener(queues = NewsUpOrDownConstants.NEWS_DOWN_FOR_ES_QUEUE)
    public void listenNewsDownMsg(String articleId){
        log.info("接收到文章下架消息， 消息内容: {}",articleId);
        // 根据 articleId 删除es索引库中对应的文章信息
        articleSearchService.deleteArticle(articleId);
    }
}
