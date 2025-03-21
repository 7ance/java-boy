package cn.lance.crypto;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class EdDsaUtilsTest {

    @Test
    public void testGenerateKeyPair() {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPair();
        System.out.println("EdDSA public key: " + keyPair.getLeft());
        System.out.println("EdDSA private key: " + keyPair.getRight());
    }

    @Test
    public void testGenerateKeyPairPem() {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPairPem();
        System.out.println("PEM EdDSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM EdDSA private key: \n" + keyPair.getRight());
    }

    @Test
    public void testSign() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPair();
        System.out.println("EdDSA public key: " + keyPair.getLeft());
        System.out.println("EdDSA private key: " + keyPair.getRight());

        String plaintext = "Hello, EdDSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = EdDsaUtils.sign(keyPair.getRight(), plaintext);
        System.out.println("EdDSA Sign: " + sign);
    }

    @Test
    public void testVerify() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPair();
        System.out.println("EdDSA public key: " + keyPair.getLeft());
        System.out.println("EdDSA private key: " + keyPair.getRight());

        String plaintext = "Hello, EdDSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = EdDsaUtils.sign(keyPair.getRight(), plaintext);
        System.out.println("EdDSA Sign: " + sign);

        boolean verified = EdDsaUtils.verify(keyPair.getLeft(), sign, plaintext);
        System.out.println("Verify result: " + verified);
        Assertions.assertTrue(verified);
    }

    @Test
    public void testSignPem() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPairPem();
        System.out.println("PEM EdDSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM EdDSA private key: \n" + keyPair.getRight());

        String plaintext = "Hello, EdDSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = EdDsaUtils.sign(keyPair.getRight(), plaintext);
        System.out.println("EdDSA Sign: " + sign);
    }

    @Test
    public void testVerifyPem() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPairPem();
        System.out.println("PEM EdDSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM EdDSA private key: \n" + keyPair.getRight());

        String plaintext = "Hello, EdDSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = EdDsaUtils.sign(keyPair.getRight(), plaintext);
        System.out.println("EdDSA Sign: " + sign);

        boolean verified = EdDsaUtils.verify(keyPair.getLeft(), sign, plaintext);
        System.out.println("Verify result: " + verified);
        Assertions.assertTrue(verified);
    }

    @Test
    public void testCheckKey() {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPair();
        System.out.println("EdDSA public key: " + keyPair.getLeft());
        System.out.println("EdDSA private key: " + keyPair.getRight());

        boolean isPublic = EdDsaUtils.isPublicKey(keyPair.getLeft());
        boolean isPrivate = EdDsaUtils.isPrivateKey(keyPair.getRight());
        System.out.println("isPublic: " + isPublic);
        System.out.println("isPrivate: " + isPrivate);
        Assertions.assertTrue(isPublic);
        Assertions.assertTrue(isPrivate);
    }

    @Test
    public void testConvertBase64ToPem() {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPair();
        System.out.println("EdDSA public key: " + keyPair.getLeft());
        System.out.println("EdDSA private key: " + keyPair.getRight());

        String publicKeyPem = EdDsaUtils.convertBase64ToPem(keyPair.getLeft());
        String privateKeyPem = EdDsaUtils.convertBase64ToPem(keyPair.getRight());
        System.out.println("PEM public key: \n" + publicKeyPem);
        System.out.println("PEM private key: \n" + privateKeyPem);
    }

    @Test
    public void testConvertPemToBase64() {
        Pair<String, String> keyPair = EdDsaUtils.generateKeyPairPem();
        System.out.println("PEM EdDSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM EdDSA private key: \n" + keyPair.getRight());

        String publicKey = EdDsaUtils.convertPemToBase64(keyPair.getLeft());
        String privateKey = EdDsaUtils.convertPemToBase64(keyPair.getRight());
        System.out.println("EdDSA public key: " + publicKey);
        System.out.println("EdDSA private key: " + privateKey);
    }

    @Test
    public void testDataSize() {
        String plaintext = "Hello, EdDSA!";
        System.out.println("Plaintext: " + plaintext);
        int length = plaintext.getBytes().length;
        System.out.println("Text bytes length: " + length);
    }

    @Test
    public void testOpenSslGeneratedKeyPair() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        String publicKey = "MCowBQYDK2VwAyEAms/tLnGF3o+WJ0S0L5bqeS+HYI1qMbvgkzdZsoR/dbk=";
        String privateKey = "MC4CAQAwBQYDK2VwBCIEICQ0HGN+I+kUOv7dAJok13qFYIrDE565DIPnoyIIrXzM";

        String plaintext = "Hello, EdDSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = EdDsaUtils.sign(privateKey, plaintext);
        System.out.println("EdDSA Sign: " + sign);

        boolean verified = EdDsaUtils.verify(publicKey, sign, plaintext);
        System.out.println("Verify result: " + verified);
        Assertions.assertTrue(verified);
    }

}
