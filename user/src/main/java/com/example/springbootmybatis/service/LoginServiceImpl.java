package com.example.springbootmybatis.service;

import com.example.springbootmybatis.dao.UserDao;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.util.DBEncryptUtil;
import com.example.springbootmybatis.util.RSAEncryptUtil;
import com.example.springbootmybatis.util.UserInfoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service(value = "loginService")
public class LoginServiceImpl implements ILoginService{
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${RSA.Encrypt.PrivateKey1}") String privateKey;

    @Override
    public JsonRespBo login(User user, HttpServletRequest request) throws Exception {
        JsonRespBo result = new JsonRespBo();
        Integer loginId = user.getId();
        System.out.println(loginId);
        String rsaPwd = user.getPwd();
        String loginPwd = RSAEncryptUtil.decrypt(rsaPwd, privateKey);
        System.out.println(loginPwd);
//        String loginPwd = user.getPwd();

//        为了防止数据权限拦截器获取不到userId报错
        redisTemplate.boundValueOps("userId").set(loginId);

        User authUser = userDao.queryUserById(loginId);
        System.out.println(authUser);
        if (authUser != null){
            boolean verified;
            if (StringUtils.isNotEmpty(authUser.getSalt())){
                verified = DBEncryptUtil.verify(loginPwd,authUser.getPwd());
            } else {
                verified = loginPwd.equals(authUser.getPwd());
            }
            if (verified) {
                Map<String, Object> data = new HashMap<>();
                String token = UserInfoUtils.createJWT(loginId);

                data.put("token", token);
                data.put("user",authUser);
                result.setData(data);
                result.setCode("1");

                redisTemplate.boundValueOps("token").set(token);
                Object name = redisTemplate.boundValueOps("token").get();
                System.out.println(name);
                redisTemplate.boundValueOps("userId").set(authUser.getId());

                List roleList = authService.getRole(authUser.getId());
                redisTemplate.delete("roleList");
                if (Objects.nonNull(roleList) && roleList.size()>0) {
                    redisTemplate.opsForList().rightPushAll("roleList",roleList);
                } else {
                    roleList = Arrays.asList("普通用户");
                    redisTemplate.opsForList().rightPushAll("roleList",roleList);
                }
                redisTemplate.expire("token", 60, TimeUnit.MINUTES);
                redisTemplate.expire("userId", 60, TimeUnit.MINUTES);
                redisTemplate.expire("roleList", 60, TimeUnit.MINUTES);
            }
            else {
                redisTemplate.delete("userId");

                Map<String, Object> data = new HashMap<>();
                data.put("token", "none");
                data.put("user",authUser);
                result.setData(data);
                result.setCode("0");
                result.setMessage("密码不正确");
                System.out.println(result);
            }
        } else {
            redisTemplate.delete("userId");

            result.setCode("0");
            result.setMessage("用户不存在");
        }
        System.out.println(result);
        return result;
    }

    @Override
    public boolean logOut(HttpServletRequest request) {
        String redisToken = (String) redisTemplate.boundValueOps("token").get();
        return redisTemplate.delete("token") && redisTemplate.delete("userId") && redisTemplate.delete("roleList");
    }
}
