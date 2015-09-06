package ru.lsv.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Кодировщик пароля (пока - для отладки)
 * 
 * @author s.lezhnev
 */
public class PasswordCrypto {

    /**
     * Password encoder
     */
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Singleton instance
     */
    private static PasswordCrypto instance;

    /**
     * Singletion
     * 
     * @return instance
     */
    public static PasswordCrypto getInstance() {
        if (instance == null) {
            instance = new PasswordCrypto();
        }

        return instance;
    }

    /**
     * Кодирует пароль
     * 
     * @param str
     *            Пароль
     * @return Закодированный пароль
     */
    public String encrypt(final String str) {
        return passwordEncoder.encode(str);
    }
}
