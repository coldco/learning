package edu.hitwh.utils;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JWTUtils {

    public static Integer currentJwt;
    private static String signKey = "hitwh";//设置密钥
    private static long expire = 3200*1000*24L;//设置令牌有效时常24个小时


    /*
    *生成jwt令牌
    * */
    public static String JwtGenerate(Map<String, Object> claims){
        log.info("开始生成jwt令牌");
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256,signKey)
                .setExpiration(new Date(System.currentTimeMillis()+expire))
                .compact();

    }

    /*
    * 解析jwt令牌
    * */

    public static Claims JwtParse(String jwt){
         return Jwts.parser().setSigningKey(signKey).parseClaimsJws(jwt).getBody();
    }
}
