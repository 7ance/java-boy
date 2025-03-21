package cn.lance.crypto;

import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.util.io.Streams;
import org.pgpainless.PGPainless;
import org.pgpainless.algorithm.DocumentSignatureType;
import org.pgpainless.decryption_verification.ConsumerOptions;
import org.pgpainless.decryption_verification.DecryptionStream;
import org.pgpainless.decryption_verification.MessageMetadata;
import org.pgpainless.encryption_signing.EncryptionOptions;
import org.pgpainless.encryption_signing.EncryptionStream;
import org.pgpainless.encryption_signing.ProducerOptions;
import org.pgpainless.encryption_signing.SigningOptions;
import org.pgpainless.key.generation.type.rsa.RsaLength;
import org.pgpainless.key.protection.SecretKeyRingProtector;
import org.pgpainless.util.Passphrase;

import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Pretty Good Privacy
 */
public class PgpUtils {

    private PgpUtils() {
    }

    /**
     * 生成PGP密钥对（ASCII）
     *
     * @param userId 用户标识
     * @param email  用户邮箱
     * @return left=公钥 right=密钥
     */
    public static Pair<String, String> generateKeyPair(String userId, String email, String passphrase)
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        Objects.requireNonNull(userId, "userId should not be null");
        Objects.requireNonNull(email, "email should not be null");
        Objects.requireNonNull(passphrase, "passphrase should not be null");

        String userIdFormatted = String.format("%s <%s>", userId, email);

        PGPSecretKeyRing pgpSecretKey = PGPainless.generateKeyRing()
                .simpleRsaKeyRing(userIdFormatted, RsaLength._4096, Passphrase.fromPassword(passphrase));

        PGPPublicKeyRing pgpPublicKey = PGPainless.extractCertificate(pgpSecretKey);

        String secretKey = PGPainless.asciiArmor(pgpSecretKey);
        String publicKey = PGPainless.asciiArmor(pgpPublicKey);

        return Pair.of(publicKey, secretKey);
    }

    /**
     * 从PGP密钥中提取出公钥
     *
     * @param secretKey PGP密钥（ASCII）
     * @return PGP公钥（ASCII）
     */
    public static String extract(String secretKey) throws IOException {
        Objects.requireNonNull(secretKey, "secretKey should not be null");

        PGPSecretKeyRing pgpSecretKey = PGPainless.readKeyRing().secretKeyRing(secretKey);
        PGPPublicKeyRing pgpPublicKey = PGPainless.extractCertificate(Objects.requireNonNull(pgpSecretKey));
        return PGPainless.asciiArmor(pgpPublicKey);
    }

    /**
     * 将PGP公钥字符串读取为对象
     *
     * @param publicKey PGP公钥（ASCII）
     * @return PGP公钥串对象
     */
    public static PGPPublicKeyRing readPublicKey(String publicKey) throws IOException {
        Objects.requireNonNull(publicKey, "publicKey should not be null");

        return PGPainless.readKeyRing().publicKeyRing(publicKey);
    }

    /**
     * 将PGP密钥字符串读取为对象
     *
     * @param secretKey PGP密钥（ASCII）
     * @return PGP密钥串对象
     */
    public static PGPSecretKeyRing readSecretKey(String secretKey) throws IOException {
        Objects.requireNonNull(secretKey, "secretKey should not be null");

        return PGPainless.readKeyRing().secretKeyRing(secretKey);
    }

    /**
     * 对文本进行签名
     *
     * @param secretKey  PGP密钥（ASCII）
     * @param passphrase 密钥口令
     * @param plaintext  明文
     * @return PGP消息
     */
    public static String sign(String secretKey, String passphrase, String plaintext)
            throws PGPException, IOException {
        Objects.requireNonNull(secretKey, "secretKey should not be null");
        Objects.requireNonNull(passphrase, "passphrase should not be null");
        Objects.requireNonNull(plaintext, "plaintext should not be null");

        PGPSecretKeyRing pgpSecretKey = PGPainless.readKeyRing().secretKeyRing(secretKey);
        Objects.requireNonNull(pgpSecretKey, "pgpSecretKey should not be null");

        try (OutputStream out = new ByteArrayOutputStream();
             InputStream plaintextInputStream = new ByteArrayInputStream(plaintext.getBytes())) {

            SecretKeyRingProtector secretKeyRingProtector = SecretKeyRingProtector.unlockEachKeyWith(
                    Passphrase.fromPassword(passphrase),
                    pgpSecretKey
            );

            EncryptionStream encryptionStream = PGPainless.encryptAndOrSign()
                    .onOutputStream(out)
                    .withOptions(
                            ProducerOptions.sign(
                                    new SigningOptions()
                                            .addInlineSignature(
                                                    secretKeyRingProtector,
                                                    pgpSecretKey,
                                                    DocumentSignatureType.BINARY_DOCUMENT)
                            ).setAsciiArmor(true)
                    );

            Streams.pipeAll(plaintextInputStream, encryptionStream);
            encryptionStream.close();

            return out.toString();
        }
    }

    /**
     * 验证签名
     *
     * @param publicKey PGP公钥（ASCII）
     * @param message   PGP消息
     * @return 验证结果 true=一致 false=不一致
     */
    public static boolean verify(String publicKey, String message) throws PGPException, IOException {
        Objects.requireNonNull(publicKey, "publicKey should not be null");
        Objects.requireNonNull(message, "message should not be null");

        PGPPublicKeyRing pgpPublicKey = PGPainless.readKeyRing().publicKeyRing(publicKey);
        Objects.requireNonNull(pgpPublicKey, "pgpPublicKey should not be null");
        try (OutputStream out = new ByteArrayOutputStream();
             InputStream encryptedInputStream = new ByteArrayInputStream(message.getBytes())) {
            DecryptionStream decryptionStream = PGPainless.decryptAndOrVerify()
                    .onInputStream(encryptedInputStream)
                    .withOptions(new ConsumerOptions()
                            .addVerificationCert(pgpPublicKey)
                    );

            Streams.pipeAll(decryptionStream, out);
            decryptionStream.close();

            MessageMetadata metadata = decryptionStream.getMetadata();
            return metadata.isVerifiedSigned();
        }
    }

    /**
     * 把PGP消息解析为明文
     *
     * @param message PGP消息
     * @return 明文
     */
    public static String decodeMessage(String message) throws PGPException, IOException {
        Objects.requireNonNull(message, "message should not be null");

        try (OutputStream out = new ByteArrayOutputStream();
             InputStream encryptedInputStream = new ByteArrayInputStream(message.getBytes())) {
            DecryptionStream decryptionStream = PGPainless.decryptAndOrVerify()
                    .onInputStream(encryptedInputStream)
                    .withOptions(new ConsumerOptions());

            Streams.pipeAll(decryptionStream, out);
            decryptionStream.close();

            return out.toString();
        }
    }

    /**
     * 对文本进行加密
     *
     * @param publicKey PGP公钥（ASCII）
     * @param plaintext 明文
     * @return PGP消息
     */
    public static String encrypt(String publicKey, String plaintext)
            throws PGPException, IOException {
        Objects.requireNonNull(publicKey, "publicKey should not be null");
        Objects.requireNonNull(plaintext, "plaintext should not be null");

        PGPPublicKeyRing pgpPublicKey = PGPainless.readKeyRing().publicKeyRing(publicKey);
        Objects.requireNonNull(pgpPublicKey, "pgpPublicKey should not be null");

        try (OutputStream out = new ByteArrayOutputStream();
             InputStream plaintextInputStream = new ByteArrayInputStream(plaintext.getBytes())) {

            EncryptionStream encryptionStream = PGPainless.encryptAndOrSign()
                    .onOutputStream(out)
                    .withOptions(
                            ProducerOptions.encrypt(
                                    new EncryptionOptions()
                                            .addRecipient(pgpPublicKey)
                            ).setAsciiArmor(true)
                    );

            Streams.pipeAll(plaintextInputStream, encryptionStream);
            encryptionStream.close();

            return out.toString();
        }
    }

    /**
     * 对消息进行解密
     *
     * @param secretKey  PGP密钥（ASCII）
     * @param passphrase 密钥口令
     * @param message    PGP消息
     * @return 明文
     */
    public static String decrypt(String secretKey, String passphrase, String message)
            throws PGPException, IOException {
        Objects.requireNonNull(secretKey, "secretKey should not be null");
        Objects.requireNonNull(passphrase, "passphrase should not be null");
        Objects.requireNonNull(message, "message should not be null");

        PGPSecretKeyRing pgpSecretKey = PGPainless.readKeyRing().secretKeyRing(secretKey);
        Objects.requireNonNull(pgpSecretKey, "pgpSecretKey should not be null");

        try (OutputStream out = new ByteArrayOutputStream();
             InputStream encryptedInputStream = new ByteArrayInputStream(message.getBytes())) {

            SecretKeyRingProtector secretKeyRingProtector = SecretKeyRingProtector.unlockEachKeyWith(
                    Passphrase.fromPassword(passphrase),
                    pgpSecretKey
            );

            DecryptionStream decryptionStream = PGPainless.decryptAndOrVerify()
                    .onInputStream(encryptedInputStream)
                    .withOptions(
                            new ConsumerOptions()
                                    .addDecryptionKey(pgpSecretKey, secretKeyRingProtector)
                    );

            Streams.pipeAll(decryptionStream, out);
            decryptionStream.close();

            return out.toString();
        }
    }

    /**
     * 对文本进行加密并签名
     *
     * @param publicKey  PGP公钥（ASCII），用于加密
     * @param secretKey  PGP密钥（ASCII），用于签名
     * @param passphrase 密钥口令
     * @param plaintext  明文
     * @return PGP消息
     */
    public static String encryptAndSign(String publicKey, String secretKey, String passphrase, String plaintext)
            throws PGPException, IOException {
        Objects.requireNonNull(publicKey, "publicKey should not be null");
        Objects.requireNonNull(secretKey, "secretKey should not be null");
        Objects.requireNonNull(passphrase, "passphrase should not be null");
        Objects.requireNonNull(plaintext, "plaintext should not be null");

        PGPPublicKeyRing pgpPublicKey = PGPainless.readKeyRing().publicKeyRing(publicKey);
        PGPSecretKeyRing pgpSecretKey = PGPainless.readKeyRing().secretKeyRing(secretKey);
        Objects.requireNonNull(pgpPublicKey, "pgpPublicKey should not be null");
        Objects.requireNonNull(pgpSecretKey, "pgpSecretKey should not be null");

        try (OutputStream out = new ByteArrayOutputStream();
             InputStream plaintextInputStream = new ByteArrayInputStream(plaintext.getBytes())) {

            SecretKeyRingProtector secretKeyRingProtector = SecretKeyRingProtector.unlockEachKeyWith(
                    Passphrase.fromPassword(passphrase),
                    pgpSecretKey
            );

            EncryptionStream encryptionStream = PGPainless.encryptAndOrSign()
                    .onOutputStream(out)
                    .withOptions(
                            ProducerOptions.signAndEncrypt(
                                    new EncryptionOptions()
                                            .addRecipient(pgpPublicKey),
                                    new SigningOptions()
                                            .addInlineSignature(
                                                    secretKeyRingProtector,
                                                    pgpSecretKey,
                                                    DocumentSignatureType.BINARY_DOCUMENT)
                            ).setAsciiArmor(true)
                    );

            Streams.pipeAll(plaintextInputStream, encryptionStream);
            encryptionStream.close();

            return out.toString();
        }
    }

    /**
     * 对消息进行解密并验证签名
     *
     * @param publicKey  PGP公钥（ASCII），用于验证签名
     * @param secretKey  PGP密钥（ASCII），用于解密
     * @param passphrase 密钥口令
     * @param message    PGP消息
     * @return 明文
     * @throws RuntimeException 签名验证失败
     */
    public static String decryptAndVerify(String publicKey, String secretKey, String passphrase, String message)
            throws PGPException, IOException {
        Objects.requireNonNull(publicKey, "publicKey should not be null");
        Objects.requireNonNull(secretKey, "secretKey should not be null");
        Objects.requireNonNull(passphrase, "passphrase should not be null");
        Objects.requireNonNull(message, "message should not be null");

        PGPPublicKeyRing pgpPublicKey = PGPainless.readKeyRing().publicKeyRing(publicKey);
        PGPSecretKeyRing pgpSecretKey = PGPainless.readKeyRing().secretKeyRing(secretKey);
        Objects.requireNonNull(pgpPublicKey, "pgpPublicKey should not be null");
        Objects.requireNonNull(pgpSecretKey, "pgpSecretKey should not be null");

        try (OutputStream out = new ByteArrayOutputStream();
             InputStream encryptedInputStream = new ByteArrayInputStream(message.getBytes())) {

            SecretKeyRingProtector secretKeyRingProtector = SecretKeyRingProtector.unlockEachKeyWith(
                    Passphrase.fromPassword(passphrase),
                    pgpSecretKey
            );

            DecryptionStream decryptionStream = PGPainless.decryptAndOrVerify()
                    .onInputStream(encryptedInputStream)
                    .withOptions(
                            new ConsumerOptions()
                                    .addDecryptionKey(pgpSecretKey, secretKeyRingProtector)
                                    .addVerificationCert(pgpPublicKey)
                    );

            Streams.pipeAll(decryptionStream, out);
            decryptionStream.close();

            MessageMetadata metadata = decryptionStream.getMetadata();
            if (!metadata.isVerifiedSigned()) {
                throw new RuntimeException("verify signature failed");
            }

            return out.toString();
        }
    }

}
