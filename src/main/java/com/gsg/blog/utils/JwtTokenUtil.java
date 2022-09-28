package com.gsg.blog.utils;

import com.gsg.blog.config.JwtProperties;
import com.gsg.blog.model.JwtUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @Author shuaigang
 * @Date 2021/9/29 13:04
 */
@Component
@Data
@Slf4j
public class JwtTokenUtil {
    // 注入自己的jwt配置
    @Resource
    private JwtProperties jwtProperties;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";

    private static final String AUDIENCE_UNKNOWN = "unknown";
    private static final String AUDIENCE_WEB = "web";
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";

    // 解析token返回map
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getBase64Secret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }
        return claims;
    }


    public String getUsernameFromToken(String token) {
        // 根据token获取用户名
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username =null;
        }
        return username;

    }

    // 获取token创建时间
    /*public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }*/


    // 获取token有效期
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            //得到token的有效期
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    // 获取jwt接收者
    /*public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }*/

    // 设置过期时间
    private Date genterateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtProperties.getTokenValidityInSeconds() * 1000);
    }

    // 判断是否过期
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        // 转换为指定日期  yyyy年MM月dd日 HH:mm:ss
        String expirationStr = DateFormateUtils.formateDate(expiration, DateFormateUtils.STANDARD_STAMP4);
        log.debug("当前Token[{}], 超时时间[{}]", token, expirationStr);
        return expiration.before(new Date());
    }

    // 生成时间是否在最后修改时间之前
    /*private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }*/

    // 忽略token有效期
    /*private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }*/


    // 生成token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    //生成token（最关键）
    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getBase64Secret())
                .setClaims(claims)
                .setExpiration(genterateExpirationDate())
                .compact();
    }

    /*public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        // 得到token生成时间
        final Date created = getCreatedDateFromToken(token);
        // 判断生成时间不在修改时间之前、token未过期并且未忽略token有效期
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }*/

    // 刷新token过期时间
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    // 验证token是否有效
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUserDetails user = (JwtUserDetails) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername())&& !isTokenExpired(token));
    }

}
