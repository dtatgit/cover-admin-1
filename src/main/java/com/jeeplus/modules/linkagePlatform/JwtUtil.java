package com.jeeplus.modules.linkagePlatform;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtUtil {

    private static String privateKey = "f35ba29cb047";

    private static Algorithm algorithm =  Algorithm.HMAC256(privateKey);

    //token过期时间
    private static Duration timeout = Duration.ofMinutes(5);

    private static String accessToken;

    /**
     * 生成accessToken
     * @return
     */
    public static String createToken() {
        if (StringUtils.isBlank(accessToken)) {
            accessToken = create();
        } else {
            if (!verify(accessToken)) {
                accessToken = create();
            }
        }
        return accessToken;
    }



    //生成token
    public static String create()
    {
        LocalDateTime localDateTime =  LocalDateTime.now().plusMinutes(timeout.toMinutes());
        Date date = Date.from( localDateTime.atZone( ZoneId.systemDefault()).toInstant());
        String token ="";
        try {
            token = JWT.create()
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }


    /**
     * 校验token是否有效
     */
    public static boolean verify(String token)
    {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return  true;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return  false;
        }
    }


    public static void main(String[] args) {
        String token = create();
        System.out.println("token1:" + createToken());
        //如果token 过期将返回false
        //System.out.println("token:" + verify(token));
    }

}
