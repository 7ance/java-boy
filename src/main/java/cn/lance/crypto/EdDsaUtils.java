package cn.lance.crypto;

import org.apache.commons.lang3.tuple.Pair;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

/**
 * Edwards-curve Digital Signature Algorithm
 */
public class EdDsaUtils {

    private final static String ALG = "Ed25519";

    private EdDsaUtils() {
    }

    /**
     * 生成EdDSA密钥对（Base64）
     *
     * @return left=公钥 right=私钥
     */
    public static Pair<String, String> generateKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALG);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyBytes);

        PrivateKey privateKey = keyPair.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKeyBytes);

        return Pair.of(publicKeyBase64, privateKeyBase64);
    }

    /**
     * 生成EdDSA密钥对（PEM）
     *
     * @return left=公钥 right=私钥
     */
    public static Pair<String, String> generateKeyPairPem() {
        Pair<String, String> keyPair = generateKeyPair();

        return Pair.of(convertBase64ToPem(keyPair.getLeft()), convertBase64ToPem(keyPair.getRight()));
    }

    /**
     * EdDSA签名
     *
     * @param privateKey 私钥（Base64）
     * @param plaintext  原文
     * @return 签名（Base64）
     */
    public static String sign(String privateKey, String plaintext) throws InvalidKeySpecException, InvalidKeyException, SignatureException {
        Objects.requireNonNull(privateKey, "privateKey must not be null");
        Objects.requireNonNull(plaintext, "plaintext must not be null");

        Signature signature;
        KeyFactory keyFactory;
        try {
            signature = Signature.getInstance(ALG);
            keyFactory = KeyFactory.getInstance(ALG);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        privateKey = convertPemToBase64(privateKey);

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PrivateKey generatedPrivateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        signature.initSign(generatedPrivateKey);
        signature.update(plaintext.getBytes());

        byte[] bytes = signature.sign();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * EdDSA验证签名
     *
     * @param publicKey 公钥（Base64）
     * @param sign      签名（Base64）
     * @param plaintext 原文
     * @return 验证结果 true=一致 false=不一致
     */
    public static boolean verify(String publicKey, String sign, String plaintext) throws InvalidKeySpecException, InvalidKeyException, SignatureException {
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        Objects.requireNonNull(sign, "sign must not be null");
        Objects.requireNonNull(plaintext, "plaintext must not be null");

        Signature signature;
        KeyFactory keyFactory;
        try {
            signature = Signature.getInstance(ALG);
            keyFactory = KeyFactory.getInstance(ALG);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        publicKey = convertPemToBase64(publicKey);

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        PublicKey generatedPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

        signature.initVerify(generatedPublicKey);
        signature.update(plaintext.getBytes());

        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 将Base64编码的密钥格式化为PEM格式的密钥
     *
     * @param base64Key 密钥（Base64）
     * @return 密钥（PEM）
     */
    public static String convertBase64ToPem(String base64Key) {
        Objects.requireNonNull(base64Key, "base64Key must not be null");

        if (isPublicKey(base64Key)) {
            StringBuilder formattedKey = new StringBuilder();
            formattedKey.append("-----BEGIN PUBLIC KEY-----\n");
            int index = 0;
            while (index < base64Key.length()) {
                formattedKey.append(base64Key, index, Math.min(index + 64, base64Key.length()))
                        .append("\n");
                index += 64;
            }
            formattedKey.append("-----END PUBLIC KEY-----\n");
            return formattedKey.toString();
        }

        if (isPrivateKey(base64Key)) {
            StringBuilder formattedKey = new StringBuilder();
            formattedKey.append("-----BEGIN PRIVATE KEY-----\n");
            int index = 0;
            while (index < base64Key.length()) {
                formattedKey.append(base64Key, index, Math.min(index + 64, base64Key.length()))
                        .append("\n");
                index += 64;
            }
            formattedKey.append("-----END PRIVATE KEY-----\n");
            return formattedKey.toString();
        }

        throw new RuntimeException("base64Key is neither a public key nor a private key");
    }

    /**
     * 将PEM格式的密钥格式化为Base64编码的密钥
     *
     * @param pemKey 密钥（PEM）
     * @return 密钥（Base64）
     */
    public static String convertPemToBase64(String pemKey) {
        Objects.requireNonNull(pemKey, "pemKey must not be null");

        return pemKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\n", "");
    }

    /**
     * 判断密钥（Base64）是不是公钥
     *
     * @param base64Key 密钥（Base64）
     * @return true=是公钥 false=不是公钥
     */
    public static boolean isPublicKey(String base64Key) {
        Objects.requireNonNull(base64Key, "base64Key must not be null");

        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(ALG);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(base64Key);
            keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            return true;
        } catch (InvalidKeySpecException e) {
            return false;
        }
    }

    /**
     * 判断密钥（Base64）是不是私钥
     *
     * @param base64Key 密钥（Base64）
     * @return true=是私钥 false=不是私钥
     */
    public static boolean isPrivateKey(String base64Key) {
        Objects.requireNonNull(base64Key, "base64Key must not be null");

        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance(ALG);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(base64Key);
            keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            return true;
        } catch (InvalidKeySpecException e) {
            return false;
        }
    }

}
