package com.heima.user.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.exception.CustException;
import com.heima.feigns.ArticleFeign;
import com.heima.feigns.WemediaFeign;
import com.heima.model.article.pojos.ApAuthor;
import com.heima.model.common.constants.admin.AdminConstants;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mrchen
 * @date 2022/4/22 11:47
 */
@Service
public class ApUserRealnameServiceImpl extends ServiceImpl<ApUserRealnameMapper, ApUserRealname> implements ApUserRealnameService {
    @Override
    public ResponseResult loadListByStatus(AuthDTO dto) {
        // 1. 校验参数  分页
        if (dto == null) {
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID);
        }
        dto.checkParam();
        // 2. 封装查询条件
        Page<ApUserRealname> pageReq = new Page<>(dto.getPage(),dto.getSize());
        LambdaQueryWrapper<ApUserRealname> queryWrapper = Wrappers.<ApUserRealname>lambdaQuery();
//        if(dto.getStatus() !=null ){
//
//        }
        queryWrapper.eq(dto.getStatus() !=null,ApUserRealname::getStatus,dto.getStatus());
        // 3. 执行查询 封装分页返回结果
        IPage<ApUserRealname> pageResult = this.page(pageReq, queryWrapper);
        return new PageResponseResult(dto.getPage(),dto.getSize(),pageResult.getTotal(),pageResult.getRecords());
    }


    @Autowired
    ApUserMapper apUserMapper;


    @GlobalTransactional(rollbackFor = Exception.class,timeoutMills = 300000)
    @Override
    public ResponseResult updateStatusById(AuthDTO dto, Short status) {
        // 1. 校验参数   (实名认证id 不能为空   )
        if(dto.getId() == null){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"实名认证id 不能为空");
        }
        // 2. 根据实名认证的id  查询 ap_user_realname数据                    ap_user_realname (userId)
        ApUserRealname userRealname = getById(dto.getId());
        // 3. 判断实名认证的状态 是否 为 待审核  ( 1, 2, 9)
        if(!AdminConstants.WAIT_AUTH.equals(userRealname.getStatus())){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_ALLOW,"实名认证的状态不是待审核状态");
        }
        // 4. 根据实名认证信息关联的apUserId 查询出 apUser信息               ap_user
        ApUser apUser = apUserMapper.selectById(userRealname.getUserId());
        if(apUser == null){
            CustException.cust(AppHttpCodeEnum.DATA_NOT_EXIST,"实名认证关联的用户信息不存在");
        }
        // 5. 修改实名认证的状态
        userRealname.setStatus(status);
        if(StringUtils.isNotBlank(dto.getMsg())){
            // 驳回原因
            userRealname.setReason(dto.getMsg());
        }
        // 修改状态完毕
        updateById(userRealname);
        // 6. 判断 状态是2 (审核失败 方法结束)  还是 9
        if(AdminConstants.FAIL_AUTH.equals(status)){
            return ResponseResult.okResult();
        }
        // 7. 如果是9 代表审核通过:
        // 8. 开通自媒体账户  (查询是否已开通过   保存自媒体账户信息)        wm_user
        WmUser wmUser = createWmUser(apUser);
        // 9.  创建作者信息 (查询是否已经创建    保存作者信息)               ap_author (userId   wmUserId)
        createApAuthor(apUser,wmUser);
        if(dto.getId().intValue() == 5){
            CustException.cust(AppHttpCodeEnum.PARAM_INVALID,"演示异常，用来演示分布式事务问题");
        }
        return ResponseResult.okResult();
    }

    @Autowired
    ArticleFeign articleFeign;

    /**
     * 远程创建作者信息
     * @param apUser
     * @param wmUser
     */
    private void createApAuthor(ApUser apUser, WmUser wmUser) {
        // 1. 根据用户id 查询作者信息是否存在
        ResponseResult<ApAuthor> result = articleFeign.findByUserId(apUser.getId());
        if(!result.checkCode()){
            // 远程调用失败
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        ApAuthor apAuthor = result.getData();
        // 2. 如果作者存在 抛出异常 提示已存在
        if(apAuthor!=null){
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"作者信息已存在");
        }
        apAuthor = new ApAuthor();
        // 3. 如果作者不存在  创建作者信息  并 远程保存
        apAuthor.setName(apUser.getName());
        apAuthor.setType(2);
        apAuthor.setUserId(apUser.getId());
        apAuthor.setCreatedTime(new Date());
        apAuthor.setWmUserId(wmUser.getId());
        ResponseResult saveResult = articleFeign.save(apAuthor);
        if(!saveResult.checkCode()){
            // 远程调用失败
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
    }

    @Autowired
    WemediaFeign wemediaFeign;

    /**
     * 开通自媒体用户信息
     * @param apUser
     * @return
     */
    private WmUser createWmUser(ApUser apUser) {
        //1. 远程根据用户名 查询自媒体用户信息
        ResponseResult<WmUser> result = wemediaFeign.findByName(apUser.getName());
//        result.checkCode() == true  远程调用成功   false 远程调用失败
        if(!result.checkCode()){
            // 远程调用失败
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        // 获取返回值
        WmUser wmUser = result.getData();
        //2. 如果已经存在   抛异常 结束方法
        if(wmUser!=null){
            CustException.cust(AppHttpCodeEnum.DATA_EXIST,"自媒体账户已存在");
        }
        //3. 如果不存在，基于apUser 创建 wmUser 并远程调用保存方法
        wmUser = new WmUser();
        wmUser.setApUserId(apUser.getId());
        wmUser.setName(apUser.getName());
        wmUser.setPassword(apUser.getPassword());
        wmUser.setSalt(apUser.getSalt());
        wmUser.setImage(apUser.getImage());
        wmUser.setPhone(apUser.getPhone());
        wmUser.setStatus(9);
        wmUser.setType(0);
        wmUser.setCreatedTime(new Date());
        ResponseResult<WmUser> saveResult = wemediaFeign.save(wmUser);
        if(!saveResult.checkCode()){
            // 远程调用失败
            CustException.cust(AppHttpCodeEnum.REMOTE_SERVER_ERROR,result.getErrorMessage());
        }
        return saveResult.getData();
    }
}
