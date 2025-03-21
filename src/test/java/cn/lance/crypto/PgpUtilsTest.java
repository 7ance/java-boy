package cn.lance.crypto;

import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;


public class PgpUtilsTest {

    @Test
    public void testGenerateKeyPair() throws Exception {
        String userId = "Foo";
        String email = "bar@gmail.com";
        String passphrase = "700101";

        Pair<String, String> keyPair = PgpUtils.generateKeyPair(userId, email, passphrase);
        System.out.println("PGP public key: \n" + keyPair.getLeft());
        System.out.println("PGP secret key: \n" + keyPair.getRight());
    }

    @Test
    public void testExtract() throws Exception {
        String userId = "Foo";
        String email = "bar@gmail.com";
        String passphrase = "700101";

        Pair<String, String> keyPair = PgpUtils.generateKeyPair(userId, email, passphrase);
        System.out.println("PGP public key: \n" + keyPair.getLeft());
        System.out.println("PGP secret key: \n" + keyPair.getRight());

        String publicKey = PgpUtils.extract(keyPair.getRight());
        System.out.println("PGP public key: \n" + publicKey);

        boolean equals = keyPair.getLeft().equals(publicKey);
        System.out.println("Comparison result: " + equals);
        Assertions.assertTrue(equals);
    }

    @Test
    public void testReadKeyPair()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        String userId = "Foo";
        String email = "bar@gmail.com";
        String passphrase = "700101";

        Pair<String, String> keyPair = PgpUtils.generateKeyPair(userId, email, passphrase);
        System.out.println("PGP public key: \n" + keyPair.getLeft());
        System.out.println("PGP secret key: \n" + keyPair.getRight());

        PGPPublicKeyRing pgpPublicKey = PgpUtils.readPublicKey(keyPair.getLeft());
        PGPSecretKeyRing pgpSecretKey = PgpUtils.readSecretKey(keyPair.getRight());
        Assertions.assertNotNull(pgpPublicKey);
        Assertions.assertNotNull(pgpSecretKey);

        System.out.println("PGP public keyring size: " + pgpPublicKey.size());
        System.out.println("PGP secret keyring size: " + pgpSecretKey.size());
    }

    @Test
    public void testSign()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        String userId = "Foo";
        String email = "bar@gmail.com";
        String passphrase = "700101";

        Pair<String, String> keyPair = PgpUtils.generateKeyPair(userId, email, passphrase);
        System.out.println("PGP public key: \n" + keyPair.getLeft());
        System.out.println("PGP secret key: \n" + keyPair.getRight());

        String plaintext = "Hello, OpenPGP!";
        System.out.println("Plaintext: " + plaintext);

        String signedMessage = PgpUtils.sign(keyPair.getRight(), passphrase, plaintext);
        System.out.println("Signed message: \n" + signedMessage);
    }

    @Test
    public void testVerify()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        String userId = "Foo";
        String email = "bar@gmail.com";
        String passphrase = "700101";

        Pair<String, String> keyPair = PgpUtils.generateKeyPair(userId, email, passphrase);
        System.out.println("PGP public key: \n" + keyPair.getLeft());
        System.out.println("PGP secret key: \n" + keyPair.getRight());

        String plaintext = "Hello, OpenPGP!";
        System.out.println("Plaintext: " + plaintext);

        String signedMessage = PgpUtils.sign(keyPair.getRight(), passphrase, plaintext);
        System.out.println("Signed message: \n" + signedMessage);

        boolean verified = PgpUtils.verify(keyPair.getLeft(), signedMessage);
        System.out.println("Verify result: " + verified);
        Assertions.assertTrue(verified);

        String decodedText = PgpUtils.decodeMessage(signedMessage);
        System.out.println("Decoded text: " + decodedText);

        boolean equals = plaintext.equals(decodedText);
        System.out.println("Text comparison result: " + equals);
        Assertions.assertTrue(equals);
    }

    @Test
    public void testEncrypt()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        String userId = "Foo";
        String email = "bar@gmail.com";
        String passphrase = "700101";

        Pair<String, String> keyPair = PgpUtils.generateKeyPair(userId, email, passphrase);
        System.out.println("PGP public key: \n" + keyPair.getLeft());
        System.out.println("PGP secret key: \n" + keyPair.getRight());

        String plaintext = "Hello, OpenPGP!";
        System.out.println("Plaintext: " + plaintext);

        String encryptedMessage = PgpUtils.encrypt(keyPair.getLeft(), plaintext);
        System.out.println("Encrypted message: \n" + encryptedMessage);
    }

    @Test
    public void testDecrypt()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        String userId = "Foo";
        String email = "bar@gmail.com";
        String passphrase = "700101";

        Pair<String, String> keyPair = PgpUtils.generateKeyPair(userId, email, passphrase);
        System.out.println("PGP public key: \n" + keyPair.getLeft());
        System.out.println("PGP secret key: \n" + keyPair.getRight());

        String plaintext = "Hello, OpenPGP!";
        System.out.println("Plaintext: " + plaintext);

        String encryptedMessage = PgpUtils.encrypt(keyPair.getLeft(), plaintext);
        System.out.println("Encrypted message: \n" + encryptedMessage);

        String decryptedText = PgpUtils.decrypt(keyPair.getRight(), passphrase, encryptedMessage);
        System.out.println("Decrypted text: " + decryptedText);
        Assertions.assertEquals(plaintext, decryptedText);
    }

    @Test
    public void testEncryptAndSign()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        String userIdAlpha = "Alpha";
        String emailAlpha = "alpha@gmail.com";
        String passphraseAlpha = "700101";

        Pair<String, String> keyPairAlpha = PgpUtils.generateKeyPair(userIdAlpha, emailAlpha, passphraseAlpha);
        System.out.println("PGP public key Alpha: \n" + keyPairAlpha.getLeft());
        System.out.println("PGP secret key Alpha: \n" + keyPairAlpha.getRight());

        String userIdBeta = "Beta";
        String emailBeta = "beta@gmail.com";
        String passphraseBeta = "700101";

        Pair<String, String> keyPairBeta = PgpUtils.generateKeyPair(userIdBeta, emailBeta, passphraseBeta);
        System.out.println("PGP public key Beta: \n" + keyPairBeta.getLeft());
        System.out.println("PGP secret key Beta: \n" + keyPairBeta.getRight());

        String plaintext = "Hello, OpenPGP!";
        System.out.println("Plaintext: " + plaintext);

        // Alpha -> Beta
        String signedMessage = PgpUtils.encryptAndSign(
                keyPairBeta.getLeft(),
                keyPairAlpha.getRight(),
                passphraseAlpha,
                plaintext
        );
        System.out.println("Signed Message: \n" + signedMessage);
    }

    @Test
    public void testDecryptAndVerify()
            throws PGPException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException {
        String userIdAlpha = "Alpha";
        String emailAlpha = "alpha@gmail.com";
        String passphraseAlpha = "700101";

        Pair<String, String> keyPairAlpha = PgpUtils.generateKeyPair(userIdAlpha, emailAlpha, passphraseAlpha);
        System.out.println("PGP public key Alpha: \n" + keyPairAlpha.getLeft());
        System.out.println("PGP secret key Alpha: \n" + keyPairAlpha.getRight());

        String userIdBeta = "Beta";
        String emailBeta = "beta@gmail.com";
        String passphraseBeta = "700101";

        Pair<String, String> keyPairBeta = PgpUtils.generateKeyPair(userIdBeta, emailBeta, passphraseBeta);
        System.out.println("PGP public key Beta: \n" + keyPairBeta.getLeft());
        System.out.println("PGP secret key Beta: \n" + keyPairBeta.getRight());

        String plaintext = "Hello, OpenPGP!";
        System.out.println("Plaintext: " + plaintext);

        // Alpha -> Beta
        String signedMessage = PgpUtils.encryptAndSign(
                keyPairBeta.getLeft(),
                keyPairAlpha.getRight(),
                passphraseAlpha,
                plaintext);
        System.out.println("Signed Message: \n" + signedMessage);

        String decryptedText = PgpUtils.decryptAndVerify(
                keyPairAlpha.getLeft(),
                keyPairBeta.getRight(),
                passphraseBeta,
                signedMessage
        );
        System.out.println("Decrypted text: " + decryptedText);

        Assertions.assertEquals(plaintext, decryptedText);
    }

}
