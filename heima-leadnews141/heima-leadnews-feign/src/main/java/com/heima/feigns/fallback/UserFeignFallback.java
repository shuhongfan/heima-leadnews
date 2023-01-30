package com.heima.feigns.fallback;
import com.heima.feigns.UserFeign;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.pojos.ApUser;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class UserFeignFallback implements FallbackFactory<UserFeign> {
    @Override
    public UserFeign create(Throwable throwable) {
        throwable.printStackTrace();
        return new UserFeign() {
            @Override
            public ResponseResult<ApUser> findUserById(Integer id) {
                log.error("Feign服务降级触发 远程调用:UserFeign  findUserById 失败,参数:{}",id);
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"远程服务调用出现异常");
            }
        };
    }
}