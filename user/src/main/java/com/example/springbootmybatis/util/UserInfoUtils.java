package com.example.springbootmybatis.util;

import com.example.springbootmybatis.dao.DataPermissionDao;
import com.example.springbootmybatis.service.AuthService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Component
public class UserInfoUtils {

    private static Logger logger = LoggerFactory.getLogger(UserInfoUtils.class);

    private static AuthService authServe;
    private static DataPermissionDao dataPerm;

    @Autowired
    private AuthService authService;
    @Autowired
    private DataPermissionDao dataPermission;

    @PostConstruct
    public void init(){
        authServe = this.authService;
        dataPerm = this.dataPermission;
    }

    public static String createJWT(Integer loginId) {
        long time = 1000*60*60*24;
        String key = "admin";
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder()
                .claim("userid",loginId)
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .signWith(signatureAlgorithm, key);
        System.out.println(builder.compact());
        return builder.compact();
    }

    public static Claims parseJWT(String jwt){
        String key = "admin";
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static boolean checkUrl(Integer userId, String requestUrl){
        final AntPathMatcher pathMatcher = new AntPathMatcher();
        List authUrl = authServe.authUrl(userId);
        for (int i = 0; i < authUrl.size(); i++){
            boolean b = pathMatcher.match(authUrl.get(i).toString(),requestUrl);
            if (b){
                return true;
            }
        }
        return false;
    }

    public static RedisTemplate<String, Object> redisTemplate() {
        RedisConnectionFactory factory = new JedisConnectionFactory(new JedisPoolConfig());
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        template.setValueSerializer(jacksonSeial);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();
        return template;
    }

    public static Integer getUserId() {
        return (Integer) redisTemplate().boundValueOps("userId").get();
    }

    public static String getToken() {
        return (String)  redisTemplate().boundValueOps("token").get();
    }

    public static List<String> getRole() {
        List roleList = redisTemplate().opsForList().range("roleList", 0, -1);
        return roleList;
    }

}
