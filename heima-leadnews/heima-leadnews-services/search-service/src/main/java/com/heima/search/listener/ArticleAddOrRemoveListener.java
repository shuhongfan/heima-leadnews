package com.heima.search.listener;

import com.heima.common.constants.message.NewsUpOrDownConstants;
import com.heima.common.exception.CustException;
import com.heima.feigns.ArticleFeign;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.vos.SearchArticleVO;
import com.heima.search.service.ArticleSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArticleAddOrRemoveListener {

    @Autowired
    private ArticleSearchService articleSearchService;

    @Autowired
    private ArticleFeign articleFeign;

    @RabbitListener(queuesToDeclare = @Queue(value = NewsUpOrDownConstants.NEWS_UP_FOR_ES_QUEUE))
    public void listenNewsUpMsg(String articleId) {
        log.info("接收到文章上架消息，消息内容：{}", articleId);

//        根据articleId查询文章信息，将文章信息 添加到es
        ResponseResult<SearchArticleVO> result = articleFeign.findArticle(Long.valueOf(articleId));
        if (!result.checkCode()) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "未查询到对应的文章数据");
        }

        articleSearchService.saveArticle(result.getData());
    }

    @RabbitListener(queuesToDeclare = @Queue(value = NewsUpOrDownConstants.NEWS_DOWN_FOR_ES_QUEUE))
    public void listenNewsDownMsg(String articleId) {
        log.info("收到文章下架消息，消息内容：{}", articleId);

        articleSearchService.deleteArticle(articleId);
    }
}
