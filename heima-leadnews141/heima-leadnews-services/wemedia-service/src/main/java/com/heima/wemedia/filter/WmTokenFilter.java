package com.heima.wemedia.filter;

import com.heima.model.threadlocal.WmThreadLocalUtils;
import com.heima.model.wemedia.pojos.WmUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author mrchen
 * @date 2022/4/24 16:28
 */
@Component
@WebFilter(value = "wmTokenFilter",urlPatterns = "/*")
@Order(-1)
@Slf4j
public class WmTokenFilter extends GenericFilterBean {
    /**
     * 过滤器的过滤方法
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 获取请求头中的userId
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String userId = request.getHeader("userId");
        // 将userId 封装为WmUser对象 存入到ThreadLocal中
        if (StringUtils.isNotBlank(userId)) {
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            WmThreadLocalUtils.setUser(wmUser);
        }
        // 放行请求
        filterChain.doFilter(servletRequest,servletResponse);
        // 请求完毕后，需要清空threadlocal中的信息
        WmThreadLocalUtils.clear();
    }
}
