package cn.lance.crypto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TotpUtilsTest {

    @Test
    public void testGenerate() {
        String issuer = "Foo";
        String accountName = "bar";

        String uri = TotpUtils.generate(issuer, accountName);
        System.out.println("OTP URI: " + uri);
    }

    @Test
    public void testGetSecretFromUri() {
        String issuer = "Foo";
        String accountName = "bar";

        String uri = TotpUtils.generate(issuer, accountName);
        System.out.println("OTP URI: " + uri);

        String secret = TotpUtils.getSecretFromUri(uri);
        System.out.println("OTP Secret: " + secret);
    }

    @Test
    public void testGenerateCode() {
        String issuer = "Foo";
        String accountName = "bar";

        String uri = TotpUtils.generate(issuer, accountName);
        System.out.println("OTP URI: " + uri);

        String secret = TotpUtils.getSecretFromUri(uri);
        System.out.println("OTP secret: " + secret);

        String passcode = TotpUtils.generateCode(secret);
        System.out.println("OTP passcode: " + passcode);
    }

    @Test
    public void testVerify() {
        String issuer = "Foo";
        String accountName = "bar";

        String uri = TotpUtils.generate(issuer, accountName);
        System.out.println("OTP URI: " + uri);

        String secret = TotpUtils.getSecretFromUri(uri);
        System.out.println("OTP secret: " + secret);

        String passcode = TotpUtils.generateCode(secret);
        System.out.println("OTP passcode: " + passcode);

        boolean verified = TotpUtils.verify(secret, passcode);
        System.out.println("OTP verified: " + verified);
        Assertions.assertTrue(verified);
    }

}
