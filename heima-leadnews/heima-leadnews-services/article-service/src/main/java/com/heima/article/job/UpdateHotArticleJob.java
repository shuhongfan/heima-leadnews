package com.heima.article.job;

import com.alibaba.fastjson.JSON;
import com.heima.article.service.HotArticleService;
import com.heima.common.constants.article.HotArticleConstants;
import com.heima.common.exception.CustException;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.message.app.AggBehaviorDTO;
import com.heima.model.message.app.NewBehaviorDTO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UpdateHotArticleJob {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HotArticleService hotArticleService;

    @XxlJob("updateHotArticleJob")
    public ReturnT updateHotArticleHandler(String params){
        log.info("热文章分值更新 调度任务开始执行....");

//        1. 获取 最近10s中 redis list队列中 产生新行为数据集合
        List<NewBehaviorDTO> newBehaviorDTOList = getRedisBehaviorList();

        if (CollectionUtils.isEmpty(newBehaviorDTOList)) {
            log.info("项目太冷清了，最近10s中没有人访问我们的项目");
            return ReturnT.SUCCESS;
        }

//        2. 分组聚合统计行为数据
        List<AggBehaviorDTO> aggBehaviorDTOList = getAggBehaviorList(newBehaviorDTOList);

//        3. 定时更新文章热度
//        for (AggBehaviorDTO aggBehaviorDTO : aggBehaviorDTOList) {
//            重新计算文章热度  更新redis缓存
//            hotArticleService.updateApArticle(aggBehaviorDTO);
//        }
        aggBehaviorDTOList.forEach(hotArticleService::updateApArticle);

        log.info("热文章分值更新 调度任务完成....");
        return ReturnT.SUCCESS;
    }

    /**
     *    2. 分组聚合统计行为数据
     * @param newBehaviorDTOList
     * @return
     */
    private List<AggBehaviorDTO> getAggBehaviorList(List<NewBehaviorDTO> newBehaviorDTOList) {
        ArrayList<AggBehaviorDTO> aggBehaviorDTOArrayList = new ArrayList<>();

//        1. 将新行为数据 按照文章id进行分组
        Map<Long, List<NewBehaviorDTO>> behaviorGroup =
                newBehaviorDTOList.stream().collect(Collectors.groupingBy(NewBehaviorDTO::getArticleId));

//        2. 统计每个分组的数据 计算成一个aggBehavior
        behaviorGroup.forEach((articleId,behaviorList)->{
//            将每个分组的多个行为数据 合并成一个 聚合数据
            Optional<AggBehaviorDTO> reduce = behaviorList.stream().map(newBehavior -> {
//                将 newBehavior 转换为 aggBehaviorDTO
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
                    default:
                        break;
                }
                return aggBehaviorDTO;
            }).reduce(new BinaryOperator<AggBehaviorDTO>() {
//                归并累加 变成一个 AggBehaviorDTO
                @Override
                public AggBehaviorDTO apply(AggBehaviorDTO aggBehaviorDTO, AggBehaviorDTO aggBehaviorDTO2) {
                    aggBehaviorDTO.setView(aggBehaviorDTO.getView() + aggBehaviorDTO2.getView());
                    aggBehaviorDTO.setLike(aggBehaviorDTO.getLike() + aggBehaviorDTO2.getLike());
                    aggBehaviorDTO.setComment(aggBehaviorDTO.getComment() + aggBehaviorDTO2.getComment());
                    aggBehaviorDTO.setCollect(aggBehaviorDTO.getCollect() + aggBehaviorDTO2.getCollect());
                    return aggBehaviorDTO;
                }
            });

            if (reduce.isPresent()) {
                aggBehaviorDTOArrayList.add(reduce.get());
            }
        });

        return aggBehaviorDTOArrayList;
    }

    /**
     * 1. 获取 最近10s中 redis list队列中 产生新行为数据集合
     * @return
     */
    private List<NewBehaviorDTO> getRedisBehaviorList() {
//        1. 创建 redis 脚本对象
        DefaultRedisScript<List> redisScript = new DefaultRedisScript<>();

        try {
//        2. 设置 脚本对象 （返回类型  脚本地址）
            redisScript.setResultType(List.class);
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis.lua")));

//        3. RedisTemplate 执行脚本
            List<String> result = redisTemplate.execute(redisScript, Arrays.asList(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_LIST));

//        4. 封装返回结果
            return result.stream().map(jsonStr -> JSON.parseObject(jsonStr, NewBehaviorDTO.class)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行lua脚本出现异常，原因：{}", e.getMessage());
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR, e.getMessage());
        }

        return null;
    }
}