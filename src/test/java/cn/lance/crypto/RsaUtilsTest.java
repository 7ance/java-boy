package cn.lance.crypto;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class RsaUtilsTest {

    @Test
    public void testGenerateKeyPair() {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());
    }

    @Test
    public void testGenerateKeyPairPem() {
        Pair<String, String> keyPair = RsaUtils.generateKeyPairPem();
        System.out.println("PEM RSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM RSA private key: \n" + keyPair.getRight());
    }

    @Test
    public void testSign() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());

        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = RsaUtils.sign(keyPair.getRight(), plaintext);
        System.out.println("RSA Sign: " + sign);
    }

    @Test
    public void testVerify() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());

        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = RsaUtils.sign(keyPair.getRight(), plaintext);
        System.out.println("RSA Sign: " + sign);

        boolean verified = RsaUtils.verify(keyPair.getLeft(), sign, plaintext);
        System.out.println("Verify result: " + verified);
        Assertions.assertTrue(verified);
    }

    @Test
    public void testSignPem() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPairPem();
        System.out.println("PEM RSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM RSA private key: \n" + keyPair.getRight());

        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = RsaUtils.sign(keyPair.getRight(), plaintext);
        System.out.println("RSA Sign: " + sign);
    }

    @Test
    public void testVerifyPem() throws InvalidKeySpecException, SignatureException, InvalidKeyException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPairPem();
        System.out.println("PEM RSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM RSA private key: \n" + keyPair.getRight());

        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);

        String sign = RsaUtils.sign(keyPair.getRight(), plaintext);
        System.out.println("RSA Sign: " + sign);

        boolean verified = RsaUtils.verify(keyPair.getLeft(), sign, plaintext);
        System.out.println("Verify result: " + verified);
        Assertions.assertTrue(verified);
    }

    @Test
    public void testEncrypt() throws IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());

        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = RsaUtils.encrypt(keyPair.getLeft(), plaintext);
        System.out.println("Ciphertext: " + ciphertext);
    }

    @Test
    public void testDecrypt() throws IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());

        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = RsaUtils.encrypt(keyPair.getLeft(), plaintext);
        System.out.println("Ciphertext: " + ciphertext);

        String generatedPlaintext = RsaUtils.decrypt(keyPair.getRight(), ciphertext);
        System.out.println("Generated plaintext: " + generatedPlaintext);
        Assertions.assertEquals(plaintext, generatedPlaintext);
    }

    @Test
    public void testEncryptPem() throws IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPairPem();
        System.out.println("PEM RSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM RSA private key: \n" + keyPair.getRight());

        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = RsaUtils.encrypt(keyPair.getLeft(), plaintext);
        System.out.println("Ciphertext: " + ciphertext);
    }

    @Test
    public void testDecryptPem() throws IllegalBlockSizeException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        Pair<String, String> keyPair = RsaUtils.generateKeyPairPem();
        System.out.println("PEM RSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM RSA private key: \n" + keyPair.getRight());

        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = RsaUtils.encrypt(keyPair.getLeft(), plaintext);
        System.out.println("Ciphertext: " + ciphertext);

        String generatedPlaintext = RsaUtils.decrypt(keyPair.getRight(), ciphertext);
        System.out.println("Generated plaintext: " + generatedPlaintext);
        Assertions.assertEquals(plaintext, generatedPlaintext);
    }

    @Test
    public void testCheckKey() {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());

        boolean isPublic = RsaUtils.isPublicKey(keyPair.getLeft());
        boolean isPrivate = RsaUtils.isPrivateKey(keyPair.getRight());
        System.out.println("isPublic: " + isPublic);
        System.out.println("isPrivate: " + isPrivate);
        Assertions.assertTrue(isPublic);
        Assertions.assertTrue(isPrivate);
    }

    @Test
    public void testConvertBase64ToPem() {
        Pair<String, String> keyPair = RsaUtils.generateKeyPair();
        System.out.println("RSA public key: " + keyPair.getLeft());
        System.out.println("RSA private key: " + keyPair.getRight());

        String publicKeyPem = RsaUtils.convertBase64ToPem(keyPair.getLeft());
        String privateKeyPem = RsaUtils.convertBase64ToPem(keyPair.getRight());
        System.out.println("PEM public key: \n" + publicKeyPem);
        System.out.println("PEM private key: \n" + privateKeyPem);
    }

    @Test
    public void testConvertPemToBase64() {
        Pair<String, String> keyPair = RsaUtils.generateKeyPairPem();
        System.out.println("PEM RSA public key: \n" + keyPair.getLeft());
        System.out.println("PEM RSA private key: \n" + keyPair.getRight());

        String publicKey = RsaUtils.convertPemToBase64(keyPair.getLeft());
        String privateKey = RsaUtils.convertPemToBase64(keyPair.getRight());
        System.out.println("RSA public key: " + publicKey);
        System.out.println("RSA private key: " + privateKey);
    }

    @Test
    public void testDataSize() {
        String plaintext = "Hello, RSA!";
        System.out.println("Plaintext: " + plaintext);
        int length = plaintext.getBytes().length;
        System.out.println("Text bytes length: " + length);
    }

}
