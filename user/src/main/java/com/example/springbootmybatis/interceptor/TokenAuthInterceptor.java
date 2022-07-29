package com.example.springbootmybatis.interceptor;

import com.example.springbootmybatis.annotation.Login;
import com.example.springbootmybatis.util.UserInfoUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class TokenAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplate redisTemplate;

    private static Logger logger = LoggerFactory.getLogger(TokenAuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        }else {
            return true;
        }

        if (annotation == null){
            return true;
        }

        String requestURI = request.getRequestURI();

        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)){
            logger.error("Token值不能为空！", requestURI);
            return false;
        } else {
            return validateToken(token, requestURI);
        }
    }

    private boolean validateToken(String token, String uri){
        boolean allowed = false;
        String redisToken = (String) redisTemplate.boundValueOps("token").get();
        System.out.println("redis中的token为"+redisToken);
        if (StringUtils.isNotEmpty(token)){
            if (StringUtils.isNotEmpty(redisToken) && token.equals(redisToken)) {
                try{
                    Claims claims = UserInfoUtils.parseJWT(token);
                    long nowTime = System.currentTimeMillis();
                    long expiredTime = claims.getExpiration().getTime();

                    if (expiredTime > nowTime){

                    }
//                    设置token过期时间为1分钟
                    redisTemplate.expire("token", 1, TimeUnit.MINUTES);
                    allowed = true;
                } catch (Exception e) {
                    logger.error("校验Token异常: {}, uri: {}", e.getMessage(), uri);
                }
            }
        }
        return allowed;
    }
}
