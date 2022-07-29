package com.example.springbootmybatis.util;

import io.jsonwebtoken.*;

import java.util.Date;

public class UserInfoUtils {

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
}
