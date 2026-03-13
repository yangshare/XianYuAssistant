package com.feijimiao.xianyuassistant.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密工具类
 * 使用AES-256-GCM模式进行加密，提供认证加密功能
 */
@Slf4j
@Component
public class AesEncryptUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // 96 bits
    private static final int GCM_TAG_LENGTH = 128; // 128 bits

    private static String encryptionKey;

    @Value("${app.encryption.key:}")
    public void setEncryptionKey(String key) {
        AesEncryptUtils.encryptionKey = key;
    }

    /**
     * 生成AES密钥（从配置的密钥派生）
     */
    private static byte[] deriveKey(String key) throws Exception {
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("加密密钥未配置，请设置 app.encryption.key");
        }
        // 使用SHA-256从密码派生256位密钥
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        return sha256.digest(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加密字符串
     *
     * @param plainText 明文
     * @return 加密后的Base64字符串（包含IV）
     */
    public String encrypt(String plainText) {
        return encryptStatic(plainText);
    }

    /**
     * 解密字符串
     *
     * @param encryptedText 加密后的Base64字符串（包含IV）
     * @return 明文
     */
    public String decrypt(String encryptedText) {
        return decryptStatic(encryptedText);
    }

    /**
     * 检查数据是否已加密
     * 通过检查是否是有效的Base64格式且长度合理来判断
     */
    public boolean isEncrypted(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(text);
            // 加密数据至少要有IV(12字节) + TAG(16字节) = 28字节
            return decoded.length >= GCM_IV_LENGTH + 16;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 静态加密方法（用于非Spring管理的类）
     */
    public static String encryptStatic(String plainText) {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }

        try {
            if (encryptionKey == null || encryptionKey.isEmpty()) {
                throw new IllegalStateException("加密密钥未配置");
            }

            byte[] key = deriveKey(encryptionKey);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);

            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);

            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            log.error("静态加密失败", e);
            throw new RuntimeException("加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * 静态解密方法（用于非Spring管理的类）
     */
    public static String decryptStatic(String encryptedText) {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }

        if (encryptionKey == null || encryptionKey.isEmpty()) {
            log.warn("加密密钥未配置，返回原文");
            return encryptedText;
        }

        try {
            byte[] key = deriveKey(encryptionKey);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);

            byte[] decoded = Base64.getDecoder().decode(encryptedText);

            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.debug("解密失败，返回原文: {}", e.getMessage());
            return encryptedText;
        }
    }

    /**
     * 生成随机加密密钥
     * 用于首次配置时生成密钥
     */
    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32]; // 256 bits
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
