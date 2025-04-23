package tmoney.co.kr.captcha.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class CryptoUtil {

    public static final String AES_CBC_PKCS_5_PADDING = "AES/CBC/PKCS5PADDING";
    public static final String AES = "AES";
    private static String SECRET_KEY = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456";

    private static final int KEY_LENGTH = 32; // AES-256용 32바이트

    public static String generateKey() {
        byte[] keyBytes = new byte[KEY_LENGTH];
        new SecureRandom().nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes).substring(0, KEY_LENGTH);
    }

    public static byte[] encrypt(byte[] plainTextData, byte[] secretKey) throws Exception {
        try {
            String iv = new String(secretKey).substring(0, 16);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS_5_PADDING);

            byte[] dataBytes = plainTextData;
            int plaintextLength = dataBytes.length;
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(secretKey, AES);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new String(Base64.getEncoder().encode(encrypted)).getBytes();

        } catch (Exception e) {
            log.error("Error encrypt message:: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String decrypt(String password) {
        try {
            byte[] cipherTextData = password.getBytes();
            byte[] secretKey = SECRET_KEY.getBytes();
            String iv = new String(secretKey).substring(0, 16);

            byte[] encrypted = Base64.getDecoder().decode(cipherTextData);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS_5_PADDING);
            SecretKeySpec keyspec = new SecretKeySpec(secretKey, AES);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original);
            return new String(originalString.getBytes());
        } catch (Exception e) {
            log.error("Error decrypting message:: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);

        }
    }


    public static String decrypt(String encodedKey, String password) {
        try {
            byte[] cipherTextData = password.getBytes();
            byte[] secretKey = encodedKey.getBytes();
            String iv = new String(secretKey).substring(0, 16);

            byte[] encrypted = Base64.getDecoder().decode(cipherTextData);

            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS_5_PADDING);
            SecretKeySpec keyspec = new SecretKeySpec(secretKey, AES);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original);
            return new String(originalString.getBytes());
        } catch (Exception e) {
            log.error("Error decrypting message:: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);

        }
    }
}
