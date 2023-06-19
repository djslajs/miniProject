package com.miniproject.crypto;

public interface PasswordEncoder {

    String encrypt( String rawPassword);

    Boolean matches( String rawPassword, String encryptedPassword);
}
