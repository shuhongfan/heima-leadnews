package com.heima.comment.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.aliyun.scan.GreenScan;
import com.heima.aliyun.scan.ScanResult;
import com.heima.comment.service.CommentService;
import com.heima.common.constants.article.HotArticleConstants;
import com.heima.common.exception.CustException;
import com.heima.feigns.UserFeign;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.comment.dtos.CommentDTO;
import com.heima.model.comment.dtos.CommentLikeDTO;
import com.heima.model.comment.dtos.CommentSaveDTO;
import com.heima.model.comment.pojos.ApComment;
import com.heima.model.comment.pojos.ApCommentLike;
import com.heima.model.comment.vos.ApCommentVo;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.message.app.NewBehaviorDTO;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.wemedia.pojos.WmNews;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.checkerframework.checker.units.qual.A;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.SpringDataMongoDB;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private GreenScan greenScan;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * ????????????
     * @return
     */
    @Override
    public ResponseResult saveComment(CommentSaveDTO dto) {
        //        1.????????????
//          ??????????????????
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }

//        2.????????????id ????????????????????? ??????????????????????????????140????????? (validated??????)
        dto.getArticleId();

//        ???????????????????????????????????????(??????: ???????????? ??????: ?????????????????????????????? ??????: ?????????????????????????????????**)
        try {
            ScanResult scanResult = greenScan.greenTextScan(dto.getContent());
            switch (scanResult.getSuggestion()){
                case "block":
                    // ??????
                    CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "????????????");
                    break;
                case "review":
                    // ??????

                    break;
                case "pass":
                    // ??????
                    break;
                default:
                    // ??????
                    break;
            }
        } catch (Exception e) {
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR, "???????????????????????????????????????");
        }

//        3. ????????????????????????????????????
        ApUser apUser = userFeign.findUserById(user.getId()).getData();
        if (apUser == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "???????????????????????????");
        }

//        4. ?????????????????????????????????mongo
        ApComment apComment = new ApComment();
        apComment.setAuthorId(0);
        apComment.setAuthorName(apUser.getName());
        apComment.setArticleId(dto.getArticleId());
        apComment.setType((short)0);
        apComment.setChannelId(0);
        apComment.setContent(dto.getContent());
        apComment.setImage(apComment.getImage());
        apComment.setLikes(0);
        apComment.setReply(0);
        apComment.setFlag((short)0);
        apComment.setLongitude(new BigDecimal("0"));
        apComment.setLatitude(new BigDecimal("0"));
        apComment.setAddress("");
        apComment.setOrd(0);
        apComment.setCreatedTime(new Date());
        apComment.setUpdatedTime(new Date());

        mongoTemplate.save(apComment);

        //        ??????????????????
        NewBehaviorDTO newBehaviorDTO = new NewBehaviorDTO();
        newBehaviorDTO.setType(NewBehaviorDTO.BehaviorType.COMMENT);
        newBehaviorDTO.setArticleId(dto.getArticleId());
        newBehaviorDTO.setAdd(1);

        rabbitTemplate.convertAndSend(HotArticleConstants.HOT_ARTICLE_SCORE_BEHAVIOR_QUEUE, JSON.toJSONString(newBehaviorDTO));
        log.info("???????????? ??????????????????????????????????????????{}", newBehaviorDTO);

        return ResponseResult.okResult();
    }

    /**
     * ????????????
     * @param dto
     * @return
     */
    @Override
    public ResponseResult like(CommentLikeDTO dto) {
//        1. ????????????  ??????id???????????? operation????????? 0 ??? 1
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN,"??????????????????");
        }

//        2. ????????????id????????????????????? ???null??????????????????
        Query queryComment = Query.query(Criteria.where("id").is(dto.getCommentId()));
        ApComment comment = mongoTemplate.findOne(queryComment, ApComment.class);
        if (comment == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "?????????????????????");
        }

//        3. ????????????????????? ????????????????????????
        RLock lock = redissonClient.getLock("likes-lock");
        lock.lock();
        try {
            Query queryCommentLike = Query.query(Criteria.where("commentId").is(dto.getCommentId())
                    .and("authorId").is(user.getId()));
            if (dto.getOperation() == 0) { // ??????
                ApCommentLike commentLike = mongoTemplate.findOne(queryCommentLike, ApCommentLike.class);

//        ??????????????? ??????????????????
                if (commentLike != null) {
                    CustException.cust(AppHttpCodeEnum.DATA_EXIST, "??????????????????");
                }

//        ???????????? ?????????????????????mongo
                commentLike = new ApCommentLike();
                commentLike.setAuthorId(user.getId());
                commentLike.setCommentId(dto.getCommentId());
                commentLike.setOperation(dto.getOperation());
                mongoTemplate.save(commentLike);

//        ????????????????????????????????????( + 1)
                comment.setLikes(comment.getLikes() + 1);
                mongoTemplate.save(comment);
            } else { // ????????????
//        4. ???????????????????????????
//        ??????????????????
                mongoTemplate.remove(queryCommentLike);

//        ????????????????????????????????????( - 1) , ???????????????????????????
                if (comment.getLikes() > 0) {
                    comment.setLikes(comment.getLikes() - 1);
                    mongoTemplate.save(comment);
                }
            }

        } finally {
            lock.unlock();
        }

//        ???????????????????????????????????????????????? ?????????key???????????????: likes
        HashMap<String, Object> map = new HashMap<>();
        map.put("likes", comment.getLikes());

        return ResponseResult.okResult(map);
    }

    /**
     * ????????????id??????????????????
     * @return
     */
    @Override
    public ResponseResult findByArticleId(CommentDTO dto) {
//        1. ????????????
//        ????????????id
        Query queryArticle = Query.query(Criteria.where("id").is(dto.getArticleId()));
        ApArticle article = mongoTemplate.findOne(queryArticle, ApArticle.class);
        if (article == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "???????????????");
        }

//        ??????size
        Integer size = dto.getSize();
        if (size == null || size <= 0) {
            size = 10;
        }

//        2. ??????Mongo????????????????????????
        List<ApComment> apCommentList=null;

//        ?????????????????????
        if (dto.getIndex().intValue() == 1) {
            Query query = Query.query(
                    Criteria.where("articleId").is(dto.getArticleId())
                            .and("flag").is(1))
                    .with(Sort.by(Sort.Direction.DESC, "likes"));
            apCommentList = mongoTemplate.find(query, ApComment.class);

//            ?????????????????????
            if (CollectionUtils.isNotEmpty(apCommentList)) {
                size = size - apCommentList.size();
                List<ApComment> normalComments = mongoTemplate.find(getQuery(dto, size), ApComment.class);
                apCommentList.addAll(normalComments);
            } else {
                apCommentList = mongoTemplate.find(getQuery(dto, size), ApComment.class);
            }
        }

//        3. ??????????????????
        ApUser user = AppThreadLocalUtils.getUser();
//        3.1 ???????????????  ????????????????????????
        if (user == null) {
            return ResponseResult.okResult(apCommentList);
        }

//        3.2 ???????????? ????????????????????????????????????????????????
//        ?????????????????????????????????ID??????
        List<String> commonIds = apCommentList.stream().map(ApComment::getId).collect(Collectors.toList());

//        ?????? ?????????????????? ????????????id??????
        List<ApCommentLike> apCommentLikes = mongoTemplate.find(Query.query(
                        Criteria.where("commentId").in(commonIds)
                                .and("authorId").is(user.getId())),
                ApCommentLike.class);

//        ???????????????????????????????????????????????????
        if (CollectionUtils.isNotEmpty(apCommentList) && CollectionUtils.isNotEmpty(apCommentLikes)) {
//            ????????????????????????id
            List<String> commentIds = apCommentLikes.stream().map(ApCommentLike::getCommentId).collect(Collectors.toList());

//            ????????????????????????comment??????commentVo
            return ResponseResult.okResult(apCommentList.stream().map(comm -> parseCommentVo(comm, commentIds)));
        }

        return ResponseResult.okResult(apCommentList);
    }

    /**
     * ??????????????????
     * @param dto
     * @param size
     * @return
     */
    private Query getQuery(CommentDTO dto, Integer size) {
        return Query.query(Criteria.where("articleId").is(dto.getArticleId())
                        .and("flag").is(0)
                        .and("createdTime").lt(dto.getMinDate()))
                .limit(size)
                .with(Sort.by(Sort.Direction.DESC, "createdTime"));
    }

    /**
     * ???comment ?????? vo??????   ??????likes???????????????????????????
     */
    private ApCommentVo parseCommentVo(ApComment apComment, List<String> commentIds) {
        ApCommentVo apCommentVo = new ApCommentVo();
        BeanUtils.copyProperties(apCommentVo, apCommentVo);

//        ??????????????????????????????
        if (commentIds.contains(apCommentVo.getId())) {
            apCommentVo.setOperation((short) 0);
        }
        return apCommentVo;
    }
}
