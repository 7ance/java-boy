package cn.lance.crypto;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Hash-Based Message Authentication Code
 */
public class HmacUtils {

    private HmacUtils() {
    }

    /**
     * 生成HMAC密钥
     *
     * @param algorithm <a href="https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#keygenerator-algorithms">算法</a>
     * @param keySize   密钥长度
     * @return 密钥（十六进制）
     */
    public static String generateKey(String algorithm, Integer keySize) throws NoSuchAlgorithmException {
        Objects.requireNonNull(algorithm, "algorithm must not be null");
        Objects.requireNonNull(keySize, "keySize must not be null");

        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        return new String(Hex.encodeHex(secretKey.getEncoded()));
    }

    /**
     * HMAC签名
     *
     * @param algorithm <a href="https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#mac-algorithms">算法</a>
     * @param key       密钥（十六进制）
     * @param plaintext 原文
     * @return 签名（十六进制）
     */
    public static String sign(String algorithm, String key, String plaintext)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Objects.requireNonNull(algorithm, "algorithm must not be null");
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(plaintext, "plaintext must not be null");

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        return Hex.encodeHexString(mac.doFinal(plaintext.getBytes()));
    }

    /**
     * HMAC验证签名
     *
     * @param algorithm <a href="https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html#mac-algorithms">算法</a>
     * @param key       密钥（十六进制）
     * @param plaintext 原文
     * @param sign      签名（十六进制）
     * @return 验证结果 true=一致 false=不一致
     */
    public static boolean verify(String algorithm, String key, String plaintext, String sign) throws NoSuchAlgorithmException, InvalidKeyException {
        Objects.requireNonNull(algorithm, "algorithm must not be null");
        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(plaintext, "plaintext must not be null");
        Objects.requireNonNull(sign, "sign must not be null");

        String currentSign = sign(algorithm, key, plaintext);
        return currentSign.equals(sign);
    }

}
