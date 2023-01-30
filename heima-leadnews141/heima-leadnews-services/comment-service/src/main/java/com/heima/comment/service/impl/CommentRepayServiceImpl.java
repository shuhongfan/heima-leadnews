package com.heima.comment.service.impl;

import com.heima.comment.service.CommentRepayService;
import com.heima.common.exception.CustException;
import com.heima.feigns.UserFeign;
import com.heima.model.comment.dtos.CommentRepayDTO;
import com.heima.model.comment.dtos.CommentRepayLikeDTO;
import com.heima.model.comment.dtos.CommentRepaySaveDTO;
import com.heima.model.comment.pojos.ApComment;
import com.heima.model.comment.pojos.ApCommentRepay;
import com.heima.model.comment.pojos.ApCommentRepayLike;
import com.heima.model.comment.vos.ApCommentRepayVO;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.threadlocal.AppThreadLocalUtils;
import com.heima.model.user.pojos.ApUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentRepayServiceImpl implements CommentRepayService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserFeign userFeign;

    @Override
    public ResponseResult loadCommentRepay(CommentRepayDTO dto) {
        // 1. 校验参数
        if (dto.getSize() == null || dto.getSize()<=0) {
            dto.setSize(10);
        }
        if (dto.getMinDate()==null) {
            dto.setMinDate(new Date());
        }
        // 2. 查询评论回复列表   commentId = ?  and  createdTime < minDate  排序 createdTime desc  limit size
        Query query = Query.query(Criteria.where("commentId").is(dto.getCommentId())
                .and("createdTime").lt(dto.getMinDate())).limit(dto.getSize())
                .with(Sort.by(Sort.Direction.DESC, "createdTime"));
        List<ApCommentRepay> apCommentRepays = mongoTemplate.find(query, ApCommentRepay.class);
        // 3. 是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        // 3.1 如果未登录  或者 回复的集合是空的  那么直接返回集合
        if (user == null || apCommentRepays == null || apCommentRepays.size() == 0) {
            return ResponseResult.okResult(apCommentRepays);
        }
        // 3.2 如果登录了  并且 集合不为空 判断 当前回复列表中 哪些回复点过赞
        List<String> commentRepayIds = apCommentRepays.stream().map(ApCommentRepay::getId).collect(Collectors.toList());
        // 用户id = user.getId    commentRepayId in 集合
        Query likedCommentRepay = Query.query(Criteria.where("authorId").is(user.getId()).and("commentRepayId").in(commentRepayIds));
        // 得到当前回复列表中 ，点过赞的集合
        List<ApCommentRepayLike> apCommentRepayLikes = mongoTemplate.find(likedCommentRepay, ApCommentRepayLike.class);
        if(apCommentRepayLikes !=null &&apCommentRepayLikes.size()>0){
            List<String> likedCommentRepayIds = apCommentRepayLikes.stream().map(ApCommentRepayLike::getCommentRepayId).collect(Collectors.toList());
            // 说明有点过赞的回复
            List<ApCommentRepayVO> apCommentRepayVoList = new ArrayList<>();
            apCommentRepays.forEach(apCommentRepay -> {
                ApCommentRepayVO apCommentRepayVo = new ApCommentRepayVO();
                BeanUtils.copyProperties(apCommentRepay,apCommentRepayVo);
                if(likedCommentRepayIds.contains(apCommentRepay.getId())){
                    apCommentRepayVo.setOperation((short)0);
                }
                apCommentRepayVoList.add(apCommentRepayVo);
            });
            return ResponseResult.okResult(apCommentRepayVoList);
        }
        return ResponseResult.okResult(apCommentRepays);
    }

    @Override
    public ResponseResult saveCommentRepay(CommentRepaySaveDTO dto) {
        //1.检查参数
        //2.判断是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        //3.安全过滤,自行实现
        //4.保存评论
        ResponseResult<ApUser> userResult = userFeign.findUserById(user.getId());
        if(userResult.getCode()!=0){
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR, userResult.getErrorMessage());
        }
        user = userResult.getData();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "当前登录信息有误");
        }
        ApCommentRepay apCommentRepay = new ApCommentRepay();
        apCommentRepay.setAuthorId(user.getId());
        apCommentRepay.setAuthorName(user.getName());
        apCommentRepay.setContent(dto.getContent());
        apCommentRepay.setCommentId(dto.getCommentId());
        apCommentRepay.setCreatedTime(new Date());
        apCommentRepay.setUpdatedTime(new Date());
        apCommentRepay.setLikes(0);
        mongoTemplate.insert(apCommentRepay);

        //****更新评论的回复数量
        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);
        apComment.setReply(apComment.getReply() + 1);
        mongoTemplate.save(apComment);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult saveCommentRepayLike(CommentRepayLikeDTO dto) {
        //1.检查参数
        //2.判断是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        //3.点赞操作
        ApCommentRepay apCommentRepay = mongoTemplate.findById(dto.getCommentRepayId(), ApCommentRepay.class);
        if(apCommentRepay == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        Query query = Query.query(Criteria.where("authorId").is(user.getId()).and("commentRepayId").is(apCommentRepay.getId()));
        // 查询是否点过赞
        ApCommentRepayLike one = mongoTemplate.findOne(query, ApCommentRepayLike.class);

        if (dto.getOperation() == 0) {
            if(one != null){
                CustException.cust(AppHttpCodeEnum.DATA_EXIST,"请勿重复点赞");
            }
            //更新评论的点赞数量
            apCommentRepay.setLikes(apCommentRepay.getLikes() + 1);
            mongoTemplate.save(apCommentRepay);
            //保存 APP评论信息点赞
            ApCommentRepayLike apCommentLike = new ApCommentRepayLike();
            apCommentLike.setAuthorId(user.getId());
            apCommentLike.setCommentRepayId(apCommentRepay.getId());
            apCommentLike.setOperation(dto.getOperation());
            mongoTemplate.save(apCommentLike);
        } else {
            //4.取消点赞
            if(one == null){
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"您还没有点赞");
            }
            //更新评论的点赞数量
            apCommentRepay.setLikes(apCommentRepay.getLikes() < 0 ? 0 : apCommentRepay.getLikes() - 1);
            mongoTemplate.save(apCommentRepay);
            //更新 APP评论信息点赞
            mongoTemplate.remove(query, ApCommentRepayLike.class);
        }
        //5.数据返回
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("likes",apCommentRepay.getLikes());
        return ResponseResult.okResult(resultMap);
    }
}