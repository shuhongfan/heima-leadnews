package com.heima.feigns.fallback;

import com.heima.feigns.WemediaFeign;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.model.wemedia.pojos.WmUser;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mrchen
 * @date 2022/4/22 15:07
 */
@Component
@Slf4j
public class WemediaFeignFallback implements FallbackFactory<WemediaFeign> {
    @Override
    public WemediaFeign create(Throwable throwable) {
        throwable.printStackTrace();
        return new WemediaFeign() {
            @Override
            public ResponseResult<WmUser> save(WmUser wmUser) {
                log.error("参数: {}",wmUser);
                log.error("自媒体 save 远程调用出错啦 ~~~ !!!! {} ",throwable.getMessage());
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
            }
            @Override
            public ResponseResult<WmUser> findByName(String name) {
                log.error("参数: {}",name);
                log.error("自媒体 findByName 远程调用出错啦 ~~~ !!!! {} ",throwable.getMessage());
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
            }

            @Override
            public ResponseResult<WmNews> findWmNewsById(Integer id) {
                log.error("参数: {}",id);
                log.error("自媒体 findWmNewsById 远程调用出错啦 ~~~ !!!! {} ",throwable.getMessage());
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
            }

            @Override
            public ResponseResult updateWmNews(WmNews wmNews) {
                log.error("参数: {}",wmNews);
                log.error("自媒体 updateWmNews 远程调用出错啦 ~~~ !!!! {} ",throwable.getMessage());
                return ResponseResult.errorResult(AppHttpCodeEnum.REMOTE_SERVER_ERROR);
            }
        };
    }
}
