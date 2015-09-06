package ru.lsv.messaging.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Репо для пользователя
 * 
 * @author s.lezhnev
 */
public interface MessagingUserRepository extends
        PagingAndSortingRepository<MessagingUser, Long> {

    /**
     * Получение юзера по логину
     * 
     * @param login
     *            Логин
     * @return Юзер
     */
    MessagingUser findByLogin(final String login);

}
