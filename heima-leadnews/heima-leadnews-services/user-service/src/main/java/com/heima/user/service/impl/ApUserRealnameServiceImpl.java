package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.constants.admin.AdminConstants;
import com.heima.common.exception.CustException;
import com.heima.feigns.ArticleFeign;
import com.heima.feigns.WemediaFeign;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.AuthDTO;
import com.heima.model.user.pojos.ApUser;
import com.heima.model.user.pojos.ApUserRealname;
import com.heima.model.wemedia.pojos.WmUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.mapper.ApUserRealnameMapper;
import com.heima.user.service.ApUserRealnameService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * app端用户认证
 */
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {

    @Autowired
    private ApUserMapper apUserMapper;

    @Autowired
    private WemediaFeign wemediaFeign;

    @Autowired
    private ArticleFeign articleFeign;

    /**
     * 根据状态查询需要认证相关的用户信息
     * @param authDTO
     * @return
     */
    @Override
    public ResponseResult loadListByStatus(AuthDTO authDTO) {
//        1. 校验参数 分页
        if (authDTO == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID);
        }
        authDTO.checkParam();

//        2. 封装查询条件
        Page<ApUserRealname> pageReq = new Page<>(authDTO.getPage(), authDTO.getSize());

        LambdaQueryWrapper<ApUserRealname> queryWrapper = Wrappers.<ApUserRealname>lambdaQuery();

        queryWrapper.eq(authDTO.getStatus() != null, ApUserRealname::getStatus, authDTO.getStatus());

//        3. 执行查询,封装分页查询结果
        IPage<ApUserRealname> page = page(pageReq, queryWrapper);
        return new PageResponseResult(authDTO.getPage(), authDTO.getSize(),page.getTotal(), page.getRecords());
    }

    /**
     * 根据状态进行审核
     * @param dto
     * @param status  2 审核失败   9 审核成功
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class) //在实名认证审核的方法上，加上seata提供的全局事务管理注解 @GlobalTransactional 注解， 开启全局事务
    @Override
    public ResponseResult updateStatusById(AuthDTO dto, Short status) {
//        1.校验参数 （实名认证id 不能为空）
        if (dto.getId() == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"实名认证id不能为空");
        }

//        2. 根据实名认证的id 查询 ap_user_realname 数据
        ApUserRealname userRealname = getById(dto.getId());

//        3. 判断实名认证的状态 是否为待审核 (1,2,9)
        if (!AdminConstants.WAIT_AUTH.equals(userRealname.getStatus())) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"市民认证的状态不是待审核状态");
        }

//        4. 根据实名认证信息关联的apUserId 查询出apUser信息
        ApUser apUser = apUserMapper.selectById(userRealname.getUserId());
        if (apUser == null) {
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"实名认证关联的用户信息不存在");
        }

//        5.修改市民认证的状态
        userRealname.setStatus(status);
        if (StringUtils.isNotBlank(dto.getMsg())) {
//            驳回原因
            userRealname.setReason(dto.getMsg());
        }
//        修改状态完毕
        updateById(userRealname);

//        6. 判断 状态是2（审核失败 方法结束） 还是9
        if (AdminConstants.FAIL_AUTH.equals(status)) {
            return ResponseResult.okResult();
        }

//        7. 如果是9 代表审核通过

//        8. 开通自媒体账户(查询是否已经开通过,保存自媒体账户信息)
        WmUser wmUser = createWmUser(apUser);

//        9. 创建作者信息(查询是否创建过,开通账户)
        createApAuthor(apUser, wmUser);

//        if (dto.getId().intValue() == 5) {
//            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"演示异常，用来演示分布式事务问题");
//        }

        return ResponseResult.okResult();
    }

    /**
     * 远程创建作者信息
     * @param apUser
     * @param wmUser
     */
    private void createApAuthor(ApUser apUser, WmUser wmUser) {
//        1. 根据用户id 查询作者信息是否存在
        ResponseResult<ApAuthor> result = articleFeign.findByUserId(apUser.getId());
        if (!result.checkCode()) {
//            远程调用失败
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }

//        2. 如果作者存在 抛出异常 提示已存在
        ApAuthor author = result.getData();
        if (apUser != null) {
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"作者信息已存在");
        }

//        3. 如果作者不存在 创建作者信息，并远程保存
        author = new ApAuthor();
        author.setCreatedTime(new Date());
        author.setName(wmUser.getName());
        author.setType(AdminConstants.AUTHOR_TYPE); // 自媒体人类型
        author.setUserId(wmUser.getApUserId()); // APP 用户ID
        author.setWmUserId(wmUser.getId()); // 自媒体用户ID
        ResponseResult resultSave = articleFeign.save(author);

        if (!resultSave.checkCode()) {
//            远程调用失败
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }
    }

    /**
     * 开通自媒体用户信息
     * @param apUser
     * @return
     */
    private WmUser createWmUser(ApUser apUser) {
//        1. 远程根据用户名 查询自媒体用户信息
        ResponseResult<WmUser> result = wemediaFeign.findByName(apUser.getName());
        if (!result.checkCode()) {
//            远程调用失败
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }

//        获取返回值
        WmUser wmUser = result.getData();

//        2. 如果已经存在 抛异常 结束方法
        if (wmUser != null) {
            CustException.cust(AppHttpCodeEnum.DATA_EXIST, "自媒体账户已存在");
        }

//        3. 如果不存在，基于apUser创建wmUser 并远程调用保存方法
        wmUser = new WmUser();
        wmUser.setName(apUser.getName());
        wmUser.setSalt(apUser.getSalt());  // 盐
        wmUser.setPassword(apUser.getPassword()); // 密码
        wmUser.setPhone(apUser.getPhone());
        wmUser.setCreatedTime(new Date());
        wmUser.setType(0); // 个人
        wmUser.setApUserId(apUser.getId());  // app端用户id
        wmUser.setStatus(AdminConstants.PASS_AUTH.intValue());

        ResponseResult<WmUser> save = wemediaFeign.save(wmUser);
        if (!save.checkCode()) {
//            远程调用失败
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR, result.getErrorMessage());
        }
        return save.getData();
    }
}
