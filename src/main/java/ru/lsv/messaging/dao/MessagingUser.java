package ru.lsv.messaging.dao;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity пользователя
 * 
 * @author s.lezhnev
 */
@Entity
@Table(name = "user")
public class MessagingUser {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Роли
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    private Set<MessagingUserRole> roles;

    /**
     * Имя
     */
    private String firstName;
    /**
     * Отчество
     */
    private String middleName;
    /**
     * Фамилия
     */
    private String lastName;
    /**
     * Email
     */
    private String email;
    /**
     * Логин
     */
    private String login;
    /**
     * Пароль
     */
    @JsonIgnore
    private String password;
    /**
     * Адресная книга
     */
    @JsonIgnore
    @ManyToMany
    private Set<MessagingUser> addressBook;
    /**
     * Сообщения
     */
    @JsonIgnore
    @OneToMany
    private Set<OneMessage> messages;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return roles
     */
    public Set<MessagingUserRole> getRoles() {
        return roles;
    }

    /**
     * Store roles
     * 
     * @param rolesIn
     *            Roles
     */
    public void setRoles(final Set<MessagingUserRole> rolesIn) {
        roles = rolesIn;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the addressBook
     */
    public Set<MessagingUser> getAddressBook() {
        return addressBook;
    }

    /**
     * @param addressBookIn
     *            the addressBook to set
     */
    public void setAddressBook(final Set<MessagingUser> addressBookIn) {
        this.addressBook = addressBookIn;
    }

    /**
     * @return messages
     */
    public Set<OneMessage> getMessages() {
        return messages;
    }

    /**
     * For JPA
     */
    protected MessagingUser() {

    }

    /**
     * Default constructor
     * 
     * @param firstNameIn
     *            Имя
     * @param middleNameIn
     *            Отчество
     * @param lastNameIn
     *            Фамилия
     * @param emailIn
     *            Почта
     * @param loginIn
     *            Логин
     * @param passwordIn
     *            Пароль
     * @param rolesIn
     *            Роли
     */
    public MessagingUser(final String firstNameIn, final String middleNameIn,
            final String lastNameIn, final String emailIn,
            final String loginIn, final String passwordIn,
            final Set<MessagingUserRole> rolesIn) {
        this.firstName = firstNameIn;
        this.middleName = middleNameIn;
        this.lastName = lastNameIn;
        this.email = emailIn;
        this.login = loginIn;
        this.password = passwordIn;
        this.roles = rolesIn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer res =
                new StringBuffer(
                        String.format(
                                "User [id=%s, firstName=%s, middleName=%s, lastName=%s, email=%s, login=%s, password=%s, roles=(",
                                id, firstName, middleName, lastName, email,
                                login, password));
        if (roles == null) {
            res.append("null");
        } else {
            for (MessagingUserRole role : roles) {
                res.append(role.getRole()).append(" ");
            }
        }
        res.append(", addressbook=");
        if (addressBook == null) {
            res.append("null");
        } else {
            for (MessagingUser user : addressBook) {
                res.append(user.getLogin() + " ");
            }
        }
        res.append(")]");
        return res.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MessagingUser) {
            MessagingUser comp = (MessagingUser) obj;
            return this.id == comp.id;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return (int) id;
    }

}
