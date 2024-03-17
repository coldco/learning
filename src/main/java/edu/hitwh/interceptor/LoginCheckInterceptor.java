package edu.hitwh.interceptor;

import com.alibaba.fastjson.JSONObject;
import edu.hitwh.response.ResultResponse;
import edu.hitwh.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获得jwt令牌

        String jwt = request.getHeader("token");
        log.info("获取jwt令牌{}",jwt);
        //2.判断令牌是否存在,如果不存在，响应错误提示信息
        if (!StringUtils.hasLength(jwt)) {
            response.getWriter().write(JSONObject.toJSONString(ResultResponse.error("NOT_LOGIN")));
            log.info("令牌不存在");
            return false;
        }

        //3.解析jwt令牌，若解析失败，响应错误提示信息
        try {
            JWTUtils.currentJwt= (Integer) JWTUtils.JwtParse(jwt).get("uid");
            log.info("令牌解析成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("令牌解析失败");
            response.getWriter().write(JSONObject.toJSONString(ResultResponse.error("NOT_LOGIN")));
            return false;
        }
        //4.解析通过，放行
        log.info("解析成功，放行");
        return true;
    }
}
