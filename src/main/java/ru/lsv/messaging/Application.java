package ru.lsv.messaging;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ru.lsv.messaging.dao.MessagingUserRole;
import ru.lsv.messaging.dao.OneMessage;
import ru.lsv.messaging.dao.OneMessageRepository;
import ru.lsv.messaging.dao.MessagingUser;
import ru.lsv.messaging.dao.MessagingUserRepository;

/**
 * Основное приложение
 * 
 * @author s.lezhnev
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableJpaRepositories(basePackages = "ru.lsv.messaging.dao")
@EntityScan(basePackages = "ru.lsv.messaging.dao")
@EnableSpringDataWebSupport
public class Application {

    /**
     * Hide default constructor
     */
    public Application() {

    }

    /**
     * Инициализатор
     * 
     * @param usrRep
     *            Репо для пользователей
     * @param msgRep
     *            Репо для сообщений
     * @return Фиг знает
     */
    @Bean
    CommandLineRunner init(final MessagingUserRepository usrRep,
            final OneMessageRepository msgRep) {
        BCryptPasswordEncoder crypto = new BCryptPasswordEncoder();
        List<MessagingUser> users = new ArrayList<>(3);
        return (evt) -> Arrays.asList("a,b,c".split(","))
                .forEach(
                        a -> {
                            MessagingUser usr =
                                    new MessagingUser(a, a, a, a, a, crypto
                                            .encode(a), null);
                            users.add(usr);
                            if (a.equals("a")) {
                                usr.setRoles(Stream.of(
                                        new MessagingUserRole(
                                                MessagingUserRole.Roles.ADMIN
                                                        .toString(), usr))
                                        .collect(Collectors.toSet()));
                            } else {
                                usr.setRoles(Stream.of(
                                        new MessagingUserRole(
                                                MessagingUserRole.Roles.USER
                                                        .toString(), usr))
                                        .collect(Collectors.toSet()));
                            }
                            Set<MessagingUser> addrBook = new HashSet<>();
                            // Формируем адресную книгу! :)
                            for (int i = 0; i < users.size() - 1; i++) {
                                addrBook.add(users.get(i));
                            }
                            usr.setAddressBook(addrBook);
                            usrRep.save(usr);
                            for (int i = 0; i < 20; i++) {
                                msgRep.save(new OneMessage(usr, usr,
                                        new Timestamp(new java.util.Date()
                                                .getTime() +
                                                i), a + i, a + i));
                            }
                        });
    }

    /**
     * Основной запуск
     * 
     * @param args
     *            Аргументы
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
