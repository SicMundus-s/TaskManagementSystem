package com.example.security.securitys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
@PropertySource("classpath:application.yaml")
public class HashingServiceImpl {

    private static final int MAX_HASH_LENGTH = 64;

    @Value("${spring.security.salt}")
    private final String salt;

    @Autowired
    public HashingServiceImpl(Environment env) {
        salt = env.getProperty("jwt.secret.access");
    }

    public byte[] hashPassword(String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            log.error("Unexpected error while calculating hash ", e);
            throw new IllegalStateException(e);
        }
        digest.update(salt.getBytes());
        byte[] hashFull = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        byte[] hash;
        if (hashFull.length > MAX_HASH_LENGTH) {
            hash = new byte[MAX_HASH_LENGTH];
            System.arraycopy(hash, 0, hashFull, 0, MAX_HASH_LENGTH);
        } else {
            hash = hashFull;
        }
        return hash;
    }
}
