package ru.lsv.messaging.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Роли пользователей
 * 
 * @author s.lezhnev
 */
@Entity
@Table(name = "user_roles")
public class MessagingUserRole {

    /**
     * Доступные роли
     * 
     * @author s.lezhnev
     */
    public static enum Roles {
        /**
         * Администратор
         */
        ADMIN,
        /**
         * Обычный пользователь
         */
        USER
    }

    /**
     * Идетификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Роль
     */
    private String role;

    /**
     * Пользователь
     */
    @ManyToOne
    private MessagingUser user;

    /**
     * For JPA
     */
    protected MessagingUserRole() {

    }

    /**
     * Default Constructor
     * 
     * @param roleIn
     *            Роль
     * @param userIn
     *            Пользователь
     */
    public MessagingUserRole(final String roleIn, final MessagingUser userIn) {
        super();
        this.role = roleIn;
        this.user = userIn;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @return the user
     */
    public MessagingUser getUser() {
        return user;
    }

}
