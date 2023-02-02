package com.heima.wemedia.filter;

import com.heima.model.threadlocal.WmThreadLocalUtils;
import com.heima.model.wemedia.pojos.WmUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 使用过滤器解析header数据并设置到当前线程中
 */
@Order(-1)
@WebFilter(filterName = "wmTokenFilter",urlPatterns = "/*")
@Slf4j
@Component
public class WmTokenFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        得到header中的信息
        String userId = request.getHeader("userId");

//        将userId 封装为WmUser对象存入到Thread Local中
        if (userId != null) {
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
//            保存到当前线程
            WmThreadLocalUtils.setUser(wmUser);
        }

//        放行请求
        filterChain.doFilter(servletRequest, servletResponse);

//        请求完毕后，需要清空threadLocal中的信息
        WmThreadLocalUtils.clear();
    }
}
