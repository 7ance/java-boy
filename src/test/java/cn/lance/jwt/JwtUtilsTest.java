package cn.lance.jwt;

import cn.lance.crypto.HmacUtils;
import cn.lance.crypto.RsaUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaimsBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.UUID;

public class JwtUtilsTest {

    @Test
    public void testGenerateHs256Key() throws NoSuchAlgorithmException {
        String algoSha256 = "HmacSHA256";
        String keySha256 = HmacUtils.generateKey(algoSha256, 256);
        System.out.println("SHA256 key: " + keySha256);
    }

    @Test
    public void testSignHs256() throws NoSuchAlgorithmException {
        String algoSha256 = "HmacSHA256";
        String keySha256 = HmacUtils.generateKey(algoSha256, 256);
        System.out.println("SHA256 key: " + keySha256);

        Claims claims = new DefaultClaimsBuilder()
                .add("str", "foobar")
                .add("number", 30)
                .add("bool", true)
                .id(UUID.randomUUID().toString())
                .subject("foo")
                .issuer("Bar")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 1000 * 60 * 60 * 24))
                .build();

        String token = JwtUtils.signHs256(keySha256, claims);
        System.out.println("Token: " + token);
    }

    @Test
    public void testParseAndVerifyHs256() throws NoSuchAlgorithmException {
        String algoSha256 = "HmacSHA256";
        String keySha256 = HmacUtils.generateKey(algoSha256, 256);
        System.out.println("SHA256 key: " + keySha256);

        Claims claims = new DefaultClaimsBuilder()
                .add("str", "foobar")
                .add("number", 30)
                .add("bool", true)
                .id(UUID.randomUUID().toString())
                .subject("foo")
                .issuer("Bar")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 1000 * 60 * 60 * 24))
                .build();

        String token = JwtUtils.signHs256(keySha256, claims);
        System.out.println("Token: " + token);

        Claims verifiedClaims = JwtUtils.parseAndVerifyHs256(keySha256, token);
        System.out.println("Verified claims: " + verifiedClaims);
    }

    @Test
    public void testGenerateRs256Key() {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());
    }

    @Test
    public void testSignRs256() throws InvalidKeySpecException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());

        Claims claims = new DefaultClaimsBuilder()
                .add("str", "foobar")
                .add("number", 30)
                .add("bool", true)
                .id(UUID.randomUUID().toString())
                .subject("foo")
                .issuer("Bar")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 1000 * 60 * 60 * 24))
                .build();

        String token = JwtUtils.signRs256(keyPair.getRight(), claims);
        System.out.println("Token: " + token);
    }

    @Test
    public void testParseAndVerifyRs256() throws InvalidKeySpecException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());

        Claims claims = new DefaultClaimsBuilder()
                .add("str", "foobar")
                .add("number", 30)
                .add("bool", true)
                .id(UUID.randomUUID().toString())
                .subject("foo")
                .issuer("Bar")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 1000 * 60 * 60 * 24))
                .build();

        String token = JwtUtils.signRs256(keyPair.getRight(), claims);
        System.out.println("Token: " + token);

        Claims verifiedClaims = JwtUtils.parseAndVerifyRs256(keyPair.getLeft(), token);
        System.out.println("Verified claims: " + verifiedClaims);
    }

}
