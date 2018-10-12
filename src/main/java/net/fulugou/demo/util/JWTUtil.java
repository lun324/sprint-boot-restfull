package net.fulugou.demo.util;

import io.jsonwebtoken.*;
import org.apache.shiro.codec.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    /**
     * 设置token过期时间为5分钟
     */
    private static final long EXPIRE_TIME = 1 * 60 * 60 * 1000;

    private static final String JWT_SECRET = "922F0EAD90889E9BB3A2A383F9CFBEB8";

    private static final byte[] encodeKey = Base64.decode(JWT_SECRET);

    private static final SecretKey secretKey = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");

    private static final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;

    /**
     * 创建 jwt
     *
     * @param id
     * @param username
     * @return
     */
    public static String createJwt(Long id, String username, String password) {
        Date now = new Date(System.currentTimeMillis());
        Date exp = new Date(now.getTime() + EXPIRE_TIME);
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setIssuer("xyz.frt")
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("id", id)
                .claim("username", username)
                .claim("password", password)
                .signWith(algorithm, secretKey);

        return builder.compact();
    }

    /**
     * 解析 jwt
     *
     * @param jwt
     * @return
     */
    public static Map<String, Object> parserJwt(String jwt) throws SignatureException {
        Map<String, Object> map = new HashMap<>();

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();

        map.put("user_id", claims.get("user_id"));
        map.put("username", claims.get("username"));
        map.put("password", claims.get("password"));

        return map;
    }

}
