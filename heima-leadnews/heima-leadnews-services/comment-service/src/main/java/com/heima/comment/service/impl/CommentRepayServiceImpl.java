package com.heima.comment.service.impl;

import com.heima.aliyun.scan.GreenScan;
import com.heima.aliyun.scan.ScanResult;
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
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private GreenScan greenScan;

    /**
     * 查看更多回复内容
     * @param dto
     * @return
     */
    @Override
    public ResponseResult loadCommentRepay(CommentRepayDTO dto) {
//        1. 校验参数
        if (dto.getSize() == null || dto.getSize() <= 0) {
            dto.setSize(10);
        }

        if (dto.getMinDate() == null) {
            dto.setMinDate(new Date());
        }

//      2. 查询评论回复列表   commentId = ?  and  createdTime < minDate  排序 createdTime desc  limit size
        Query query = Query.query(Criteria.where("commentId").is(dto.getCommentId())
                .and("createdTime").lt(dto.getMinDate()))
                .limit(dto.getSize())
                .with(Sort.by(Sort.Direction.DESC,"createdTime"));
        List<ApCommentRepay> apCommentRepays = mongoTemplate.find(query, ApCommentRepay.class);

//        3.是否登录
        ApUser user = AppThreadLocalUtils.getUser();

//        3.1 如果未登录 或者 回复的集合是空的 那么直接返回集合
        if (user == null || apCommentRepays == null || apCommentRepays.size() == 0) {
            return ResponseResult.okResult(apCommentRepays);
        }

//        3.2 如果登录了 并且集合不为空 判断当前回复列表中 哪些回复过点赞
        List<String> collectRepayIds = apCommentRepays.stream().map(ApCommentRepay::getId).collect(Collectors.toList());

//      用户id = user.getId    commentRepayId in 集合
        Query likedCommentRepay = Query.query(Criteria.where("authorId").is(user.getId()).and("commentRepayId").in(collectRepayIds));

//        得到当前回复列表中，点赞的集合
        List<ApCommentRepayLike> apCommentRepayLikes = mongoTemplate.find(likedCommentRepay, ApCommentRepayLike.class);

        if (apCommentRepayLikes != null && apCommentRepayLikes.size() > 0) {
            List<String> likedCommentRepayIds = apCommentRepayLikes.stream().map(ApCommentRepayLike::getCommentRepayId).collect(Collectors.toList());

//            说明有点过赞的回复
            ArrayList<ApCommentRepayVO> repayVOArrayList = new ArrayList<>();
            apCommentRepays.forEach(apCommentRepay -> {
                ApCommentRepayVO apCommentRepayVO = new ApCommentRepayVO();
                BeanUtils.copyProperties(apCommentRepay, apCommentRepayVO);
                if (likedCommentRepayIds.contains(apCommentRepay.getId())) {
                    apCommentRepayVO.setOperation((short) 0);
                }
                repayVOArrayList.add(apCommentRepayVO);
            });
            return ResponseResult.okResult(repayVOArrayList);
        }

        return ResponseResult.okResult(apCommentRepays);
    }

    /**
     * 保存回复
     * @return
     */
    @Override
    public ResponseResult saveCommentRepay(CommentRepaySaveDTO dto) {
//        1.检查参数
//        2.判断是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            return ResponseResult.okResult(AppHttpCodeEnum.NEED_LOGIN);
        }

//        3.安全过滤
        try {
            ScanResult scanResult = greenScan.greenTextScan(dto.getContent());
            switch (scanResult.getSuggestion()){
                case "block":
                    // 失败
                    CustException.cust(AppHttpCodeEnum.PARAM_INVALID, "回复非法");
                    break;
                case "review":
                    // 人工

                    break;
                case "pass":
                    // 成功
                    break;
                default:
                    // 人工
                    break;
            }
        } catch (Exception e) {
            CustException.cust(AppHttpCodeEnum.SERVER_ERROR,"阿里云安全服务错误");
        }

//        4. 保存评论
        ResponseResult<ApUser> userResult = userFeign.findUserById(user.getId());
        if (userResult.getCode() != 0) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN, userResult.getErrorMessage());
        }
        user = userResult.getData();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN, "当前登录信息错误");
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

//        更新评论的回复数量
        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);
        apComment.setReply(apComment.getReply() + 1);
        mongoTemplate.save(apComment);

        return ResponseResult.okResult();
    }

    /**
     * 点赞回复的评论
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveCommentRepayLike(CommentRepayLikeDTO dto) {
//        1. 检查参数
//        2. 判断是否登录
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null) {
            CustException.cust(AppHttpCodeEnum.NEED_LOGIN);
        }

//        3.点赞操作
        ApCommentRepay apCommentRepay = mongoTemplate.findById(dto.getCommentRepayId(), ApCommentRepay.class);
        if (apCommentRepay == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"评论不存在");
        }

//        查询当前用户是否点过赞
        Query query = Query.query(Criteria.where("commentRepayId").is(dto.getCommentRepayId()).and("authorId").is(user.getId()));
        ApCommentRepayLike repayLike = mongoTemplate.findOne(query, ApCommentRepayLike.class);

//        点赞
        if (dto.getOperation() == 0) {
            if (repayLike != null) {
                CustException.cust(AppHttpCodeEnum.DATA_EXIST, "请勿重复点赞");
            }

//            更新评论的点赞数量
            apCommentRepay.setLikes(apCommentRepay.getLikes() + 1);
            mongoTemplate.save(apCommentRepay);

//            保存APP评论信息点赞
            ApCommentRepayLike apCommentRepayLike = new ApCommentRepayLike();
            apCommentRepayLike.setAuthorId(user.getId());
            apCommentRepayLike.setCommentRepayId(apCommentRepay.getId());
            apCommentRepayLike.setOperation(dto.getOperation());
            mongoTemplate.save(apCommentRepayLike);
        } else {
//            4. 取消点赞
            if (repayLike == null) {
                CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST, "您还没有点赞");
            }

//            更新评论的点赞数量
            apCommentRepay.setLikes(apCommentRepay.getLikes() < 0 ? 0 : apCommentRepay.getLikes() - 1);
            mongoTemplate.save(apCommentRepay);

//            更新APP评论信息点赞
            mongoTemplate.remove(query, ApCommentRepayLike.class);
        }

        //5.数据返回
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("likes",apCommentRepay.getLikes());
        return ResponseResult.okResult(resultMap);
    }
}
