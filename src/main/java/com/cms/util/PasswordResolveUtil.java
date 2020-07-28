package com.cms.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**

 * Created by i82325 on 7/8/2020.
 */
@Service
public class PasswordResolveUtil {
    @Value("${jwt.get.token.uri}")
    private String secret;

    private final String HMAC_SHA512 = "HmacSHA512";

    public String getResolvedPassword(String username, String password) {
        Mac sha512Hmac;
        String encoded = Base64.getEncoder().encodeToString(username.getBytes());
        try {
            final byte[] byteKey = password.getBytes(StandardCharsets.UTF_8);
            sha512Hmac = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(secret.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(macData) + "$" + new String(encoded);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
