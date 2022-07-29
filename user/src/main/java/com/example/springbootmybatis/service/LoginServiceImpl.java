package com.example.springbootmybatis.service;

import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.util.RSAEncryptUtil;
import com.example.springbootmybatis.util.UserInfoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service(value = "loginService")
public class LoginServiceImpl implements ILoginService{
    @Autowired
    private UserService userService;

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
        User authUser = userService.queryUserById(loginId);
        System.out.println(authUser);
        if (authUser != null){
            if (authUser.getPwd().equals(loginPwd)) {
                Map<String, Object> data = new HashMap<>();
                String token = UserInfoUtils.createJWT(loginId);

                data.put("token", token);
                data.put("user",authUser);
                result.setData(data);
                result.setCode("1");

                redisTemplate.boundValueOps("token").set(token);
                Object name = redisTemplate.boundValueOps("token").get();
                System.out.println(name);
            }
            else {
                result.setCode("0");
                result.setMessage("密码不正确");
            }
        } else {
            result.setCode("0");
            result.setMessage("用户不存在");
        }
        return result;
    }

    @Override
    public boolean logOut(HttpServletRequest request) {
        String redisToken = (String) redisTemplate.boundValueOps("token").get();
        if (StringUtils.isNotEmpty(redisToken)){
            return redisTemplate.delete("token");
        }else{
            return true;
        }
    }
}
