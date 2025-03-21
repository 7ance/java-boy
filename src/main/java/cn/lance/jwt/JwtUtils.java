package cn.lance.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * JSON Web Token
 */
public class JwtUtils {

    public static final String ALG_HS256 = "HmacSHA256";
    public static final String ALG_RS256 = "RSA";

    private JwtUtils() {
    }

    /**
     * HS256算法，生成JWT
     *
     * @param key    密钥
     * @param claims payload内容
     * @return JWT
     */
    public static String signHs256(String key, Claims claims) {
        Objects.requireNonNull(key, "key can not be null");
        Objects.requireNonNull(claims, "claims can not be null");

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALG_HS256);
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKeySpec)
                .compact();
    }

    /**
     * HS256算法，校验JWT
     *
     * @param key   密钥
     * @param token JWT
     * @return payload内容
     */
    public static Claims parseAndVerifyHs256(String key, String token) {
        Objects.requireNonNull(key, "key can not be null");
        Objects.requireNonNull(token, "token can not be null");

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ALG_HS256);
        return Jwts.parser()
                .verifyWith(secretKeySpec).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * RS256算法，生成JWT
     *
     * @param privateKey 私钥（Base64）
     * @param claims     payload内容
     * @return JWT
     */
    public static String signRs256(String privateKey, Claims claims) throws InvalidKeySpecException {
        Objects.requireNonNull(privateKey, "privateKey can not be null");
        Objects.requireNonNull(claims, "claims can not be null");

        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(ALG_RS256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PrivateKey generatedPrivateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        return Jwts.builder()
                .claims(claims)
                .signWith(generatedPrivateKey)
                .compact();
    }

    /**
     * RS256算法，校验JWT
     *
     * @param publicKey 公钥（Base64）
     * @param token     JWT
     * @return payload内容
     */
    public static Claims parseAndVerifyRs256(String publicKey, String token) throws InvalidKeySpecException {
        Objects.requireNonNull(publicKey, "publicKey can not be null");
        Objects.requireNonNull(token, "token can not be null");

        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(ALG_RS256);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        PublicKey generatedPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        return Jwts.parser()
                .verifyWith(generatedPublicKey).build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
