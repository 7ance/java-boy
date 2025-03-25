package cn.lance.crypto;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

/**
 * Time-Based One-Time Password
 */
public class TotpUtils {

    /**
     * 字节数组还需要经过Base32编码，Base32是5个字节一组，所以长度选择5的倍数
     */
    private static final int SEED_LENGTH_IN_BYTE = 20;

    private static final String ALGORITHM = "HmacSHA1";

    private static final int PASSCODE_LENGTH = 6;

    private static final int PERIOD = 30;

    /**
     * 密码最大长度
     * 要跟 {@link #DIGITS_POWER} 保持同步
     */
    private static final int MAX_PASSCODE_LENGTH = 9;

    /**
     * 10的整数幂，为了避免Math.pow()实现不正确导致 10^6 != 1000000 之类的情况
     * 要跟 {@link #MAX_PASSCODE_LENGTH} 保持同步
     */
    private static final int[] DIGITS_POWER =
            {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

    /**
     * 允许的步长（默认3，最大17）
     */
    private static final int ADJACENT_INTERVALS = 3;

    private TotpUtils() {
    }

    /**
     * 生成URI
     *
     * @param issuer      签发者
     * @param accountName 账号名称
     * @return URI
     */
    public static String generate(String issuer, String accountName) {
        Objects.requireNonNull(issuer, "issuer must not be null");
        Objects.requireNonNull(accountName, "accountName must not be null");

        return String.format("otpauth://totp/%s:%s?secret=%s", issuer, accountName, generateSecret());
    }

    /**
     * 从URI中获取密钥
     *
     * @param uri URI
     * @return 密钥（Base32）
     */
    public static String getSecretFromUri(String uri) {
        Objects.requireNonNull(uri, "uri must not be null");

        String query = URI.create(uri).getQuery();
        if (query == null || query.isEmpty()) {
            throw new RuntimeException("query must not be null");
        }

        String[] queryKeyValueArray = query.split("&");
        return Arrays.stream(queryKeyValueArray)
                .filter(keyValue -> keyValue.contains("secret="))
                .findFirst()
                .map(keyValue -> keyValue.split("=")[1])
                .orElseThrow(() -> new RuntimeException("query must contain 'secret'"));
    }

    /**
     * 生成密码
     *
     * @param secret 密钥（Base32）
     * @return 密码
     */
    public static String generateCode(String secret) {
        Objects.requireNonNull(secret, "secret must not be null");

        long currentInterval = System.currentTimeMillis() / 1000 / PERIOD;

        return generateCode(secret, counterToBytes(currentInterval));
    }

    /**
     * 校验密码是否有效
     *
     * @param secret   密钥（Base32）
     * @param passcode 密码
     * @return true=通过 false=不通过
     */
    public static boolean verify(String secret, String passcode) {
        Objects.requireNonNull(secret, "secret must not be null");
        Objects.requireNonNull(passcode, "passcode must not be null");

        if (passcode.length() > MAX_PASSCODE_LENGTH) {
            throw new RuntimeException("passcode length exceeds max length" + MAX_PASSCODE_LENGTH);
        }

        long currentInterval = System.currentTimeMillis() / 1000 / PERIOD;

        int pastIntervals = Math.max(ADJACENT_INTERVALS, 0);
        int futureIntervals = Math.max(ADJACENT_INTERVALS, 0);

        // verify pass intervals before current time, verify future intervals after
        for (int i = -pastIntervals; i <= futureIntervals; i++) {
            long counter = currentInterval + i;
            String candidate = generateCode(secret, counterToBytes(counter));
            if (candidate.equals(passcode)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 生成密钥
     *
     * @return 密钥（Base32）
     */
    private static String generateSecret() {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[SEED_LENGTH_IN_BYTE];
        random.nextBytes(randomBytes);

        Base32 codec = new Base32();
        byte[] encodedSecretBytes = codec.encode(randomBytes);
        return new String(encodedSecretBytes);
    }

    /**
     * 生成密码
     *
     * @param secret  密钥（Base32）
     * @param counter 时间戳
     * @return 密码
     */
    private static String generateCode(String secret, byte[] counter) {
        Base32 codec = new Base32();
        byte[] secretBytes = codec.decode(secret);

        byte[] result = hash(secretBytes, counter);
        if (result == null) {
            throw new RuntimeException("could not produce OTP passcode");
        }

        int offset = result[result.length - 1] & 0xf;
        int binary = ((result[offset] & 0x7f) << 24) |
                ((result[offset + 1] & 0xff) << 16) |
                ((result[offset + 2] & 0xff) << 8) |
                ((result[offset + 3] & 0xff));
        int passcode = binary % DIGITS_POWER[PASSCODE_LENGTH];

        // 位数不足，前面补0
        String format = "%0" + PASSCODE_LENGTH + "d";
        return String.format(format, passcode);
    }

    /**
     * 哈希计算
     *
     * @param secret  密钥字节数组
     * @param message 时间戳
     * @return 哈希字节数组
     */
    private static byte[] hash(byte[] secret, byte[] message) {
        try {
            Mac hmac = Mac.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(secret, "RAW");
            hmac.init(keySpec);
            return hmac.doFinal(message);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将long类型转为byte数组
     *
     * @param counter 计时器
     * @return 计时器字节数组
     */
    private static byte[] counterToBytes(long counter) {
        byte[]
                buffer = new byte[Long.SIZE / Byte.SIZE];
        for (int i = 7; i >= 0; i--) {
            buffer[i] = (byte) (counter & 0xff);
            counter = counter >> 8;
        }
        return buffer;
    }

}
