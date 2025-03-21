package cn.lance.crypto;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;

public class AesUtilsTest {

    @Test
    public void testGenerateKey() {
        String key = AesUtils.generateKey();
        System.out.println("AES key: " + key);
    }

    @Test
    public void testEncrypt() throws DecoderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String key = AesUtils.generateKey();
        System.out.println("AES key: " + key);

        String plaintext = "Hello, AES!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = AesUtils.encrypt(key, plaintext);
        System.out.println("AES ciphertext: " + ciphertext);
    }

    @Test
    public void textDecrypt() throws DecoderException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String key = AesUtils.generateKey();
        System.out.println("AES key: " + key);

        String plaintext = "Hello, AES!";
        System.out.println("Plaintext: " + plaintext);

        String ciphertext = AesUtils.encrypt(key, plaintext);
        System.out.println("AES ciphertext: " + ciphertext);

        String decryptedText = AesUtils.decrypt(key, ciphertext);
        System.out.println("AES decryptedText: " + decryptedText);

        boolean result = plaintext.equals(decryptedText);
        System.out.println("Plaintext equals to decryptedText? " + result);
        Assertions.assertTrue(result);
    }

}
