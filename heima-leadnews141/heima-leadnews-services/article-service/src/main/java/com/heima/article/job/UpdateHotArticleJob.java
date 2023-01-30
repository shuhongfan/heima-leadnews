package com.heima.article.job;
import com.alibaba.fastjson.JSON;
import com.heima.article.service.HotArticleService;
import com.heima.common.exception.CustException;
import com.heima.model.common.constants.article.HotArticleConstants;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.mess.app.AggBehaviorDTO;
import com.heima.model.mess.app.NewBehaviorDTO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UpdateHotArticleJob {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    HotArticleService hotArticleService;

    @XxlJob("updateHotArticleJob")
    public ReturnT updateHotArticleHandler(String params){
        log.info("热文章分值更新 调度任务开始执行....");
        // 1. 获取 最近10s中  redis list队列中 产生的新行为数据集合
        List<NewBehaviorDTO> newBehaviorList = getRedisBehaviorList();
        if (CollectionUtils.isEmpty(newBehaviorList)){
            log.info(" 项目 太冷清了  ，最近10s中，没人访问我们的项目~~~~~~");
            return ReturnT.SUCCESS;
        }
        // 2. 分组聚合统计行为数据
        List<AggBehaviorDTO> aggBehaviorList = getAggBehaviorList(newBehaviorList);
        // 3. 定时更新文章热度
//        for (AggBehaviorDTO aggBehaviorDTO : aggBehaviorList) {
//            hotArticleService.updateApArticle(aggBehaviorDTO);
//        }
        aggBehaviorList.forEach(hotArticleService::updateApArticle);
        log.info("热文章分值更新 调度任务完成....");
        return ReturnT.SUCCESS;
    }

    private List<AggBehaviorDTO> getAggBehaviorList(List<NewBehaviorDTO> newBehaviorList) {
        List<AggBehaviorDTO> aggBehaviorList = new ArrayList<>();
        // 1. 将新行为数据 按照文章id进行分组
        Map<Long, List<NewBehaviorDTO>> behaviorGroup = newBehaviorList.stream()
                .collect(Collectors.groupingBy(NewBehaviorDTO::getArticleId));
        // 2. 统计每个分组的数据  计算成一个aggBehavior
        behaviorGroup.forEach((articleId,behaviorList)->{
            // 将每个分组的多个行为数据(newBehavior) 合并成一个 聚合数据(AggBehavior)
            Optional<AggBehaviorDTO> reduce = behaviorList.stream()
                    .map(newBehavior -> {
                        // {type: VIEWS, articleId:123,add:1}
                        // {articleId: 123, views: 1, likes: 0 , comment: 0 , collect: 0}
                        AggBehaviorDTO aggBehaviorDTO = new AggBehaviorDTO();
                        aggBehaviorDTO.setArticleId(newBehavior.getArticleId());
                        switch (newBehavior.getType()) {
                            case LIKES:
                                aggBehaviorDTO.setLike(newBehavior.getAdd());
                                break;
                            case VIEWS:
                                aggBehaviorDTO.setView(newBehavior.getAdd());
                                break;
                            case COMMENT:
                                aggBehaviorDTO.setComment(newBehavior.getAdd());
                                break;
                            case COLLECTION:
                                aggBehaviorDTO.setCollect(newBehavior.getAdd());
                                break;
                            default:
                        }
                        return aggBehaviorDTO;
                    }).reduce(new BinaryOperator<AggBehaviorDTO>() {
                        // {articleId: 123, views: 2, likes: 1, comment: 1 , collect: 0}    a1
                        @Override
                        public AggBehaviorDTO apply(AggBehaviorDTO a1, AggBehaviorDTO a2) {
                            a1.setView(a1.getView() + a2.getView());
                            a1.setLike(a1.getLike() + a2.getLike());
                            a1.setComment(a1.getComment() + a2.getComment());
                            a1.setCollect(a1.getCollect() + a2.getCollect());
                            return a1;
                        }
                    });
            if (reduce.isPresent()) {
                aggBehaviorList.add(reduce.get());
            }
        });
        return aggBehaviorList;
    }

    /**
     * 获取 最近10s中  redis list队列中 产生的新行为数据集合
     * @return
     */
    private List<NewBehaviorDTO> getRedisBehaviorList() {
        // 1. 创建 redis脚本对象
        DefaultRedisScript<List> redisScript = new DefaultRedisScript<>();
        // 2. 设置 脚本对象(返回类型  脚本地址)
        try {
            redisScript.setResultType(List.class);
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis.lua")));
            // 3. redisTemplate执行脚本
            List<String> result = redisTemplate.execute(redisScript, Arrays.asList(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_LIST));
            // 4. 封装返回结果
            return result.stream()
                    .map(jsonStr -> JSON.parseObject(jsonStr,NewBehaviorDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行lua脚本出现异常 , 原因: {}",e.getMessage());
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,"执行lua脚本出现异常");
            return  null;
        }
    }
}