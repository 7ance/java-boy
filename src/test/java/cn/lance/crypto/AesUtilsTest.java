package cn.lance.crypto;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class AesUtilsTest {

    @Test
    public void testGenerateKey() {
        String key = AesUtils.generateKey();
        System.out.println("AES key: " + key);
    }

    @Test
    public void testGenerateIv() {
        String iv = AesUtils.generateIv();
        System.out.println("AES iv: " + iv);
    }

    @Test
    public void testEncryptWithECB() throws DecoderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String key = AesUtils.generateKey();
        System.out.println("AES key: " + key);

        String plaintext = "Hello, AES!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = AesUtils.encrypt(key, plaintext);
        System.out.println("AES ECB ciphertext: " + ciphertext);
    }

    @Test
    public void textDecryptWithECB() throws DecoderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String key = AesUtils.generateKey();
        System.out.println("AES key: " + key);

        String plaintext = "Hello, AES!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = AesUtils.encrypt(key, plaintext);
        System.out.println("AES ECB ciphertext: " + ciphertext);

        String decryptedText = AesUtils.decrypt(key, ciphertext);
        System.out.println("AES ECB decryptedText: " + decryptedText);

        boolean result = plaintext.equals(decryptedText);
        System.out.println("ECB Plaintext equals to decryptedText? " + result);
        Assertions.assertTrue(result);
    }

    @Test
    public void testEncryptWithCBC() throws DecoderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String key = AesUtils.generateKey();
        System.out.println("AES key: " + key);

        String iv = AesUtils.generateIv();
        System.out.println("AES iv: " + iv);

        String plaintext = "Hello, AES!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = AesUtils.encrypt(key, iv, plaintext);
        System.out.println("AES CBC ciphertext: " + ciphertext);
    }

    @Test
    public void textDecryptWithCBC() throws DecoderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String key = AesUtils.generateKey();
        System.out.println("AES key: " + key);

        String iv = AesUtils.generateIv();
        System.out.println("AES iv: " + iv);

        String plaintext = "Hello, AES!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = AesUtils.encrypt(key, iv, plaintext);
        System.out.println("AES CBC ciphertext: " + ciphertext);

        String decryptedText = AesUtils.decrypt(key, iv, ciphertext);
        System.out.println("AES CBC decryptedText: " + decryptedText);

        boolean result = plaintext.equals(decryptedText);
        System.out.println("CBC Plaintext equals to decryptedText? " + result);
        Assertions.assertTrue(result);
    }

}
