package ru.lsv.messaging.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Репо для сообщений
 * 
 * @author s.lezhnev
 */
@Repository
public interface OneMessageRepository extends
        PagingAndSortingRepository<OneMessage, Long> {

    /**
     * Получение ВСЕХ записей
     * 
     * @param page
     *            Страница
     * @return Сообщения
     */
    Page<OneMessage> findAll(Pageable page);

    /**
     * Получение принятых / отправленных сообщений для пользователя
     * 
     * @param fromUser
     *            Пользователь "от"
     * @param toUser
     *            Пользователь "от кого"
     * @param page
     *            Страница
     * @return Сообщение
     */
    Page<OneMessage> findByToWhomOrFromWhom(MessagingUser toUser,
            MessagingUser fromUser, Pageable page);

}
