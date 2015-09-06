package ru.lsv.messaging;

import java.sql.Timestamp;

import org.springframework.core.convert.converter.Converter;

import ru.lsv.messaging.dao.MessagingUser;
import ru.lsv.messaging.dao.OneMessage;

/**
 * Урезанное представление для OneMessage и MessagingUser
 * 
 * @author s.lezhnev
 */
public class SimpleViews {

    /**
     * Урезанное представление для MessagingUser
     * 
     * @author s.lezhnev
     */
    public static class SimpleMessagingUser {
        /**
         * Id
         */
        private long id;
        /**
         * Логин
         */
        private String login;

        /**
         * @return the id
         */
        public long getId() {
            return id;
        }

        /**
         * @return the login
         */
        public String getLogin() {
            return login;
        }

        /**
         * Default constructor
         * 
         * @param idIn
         *            Id
         * @param loginIn
         *            Логин
         */
        public SimpleMessagingUser(final long idIn, final String loginIn) {
            super();
            this.id = idIn;
            this.login = loginIn;
        }

    }

    /**
     * Урезанное представление для OneMessage
     * 
     * @author s.lezhnev
     */
    public static class SimpleOneMessage {
        /**
         * Идетификатор
         */
        private long id;

        /**
         * От кого
         */
        private SimpleMessagingUser fromWhom;
        /**
         * Кому
         */
        private SimpleMessagingUser toWhom;
        /**
         * Время
         */
        private Timestamp time;
        /**
         * Тема
         */
        private String subject;

        /**
         * @return the id
         */
        public long getId() {
            return id;
        }

        /**
         * @return the fromWhom
         */
        public SimpleMessagingUser getFromWhom() {
            return fromWhom;
        }

        /**
         * @return the toWhom
         */
        public SimpleMessagingUser getToWhom() {
            return toWhom;
        }

        /**
         * @return the time
         */
        public Timestamp getTime() {
            return time;
        }

        /**
         * @return the subject
         */
        public String getSubject() {
            return subject;
        }

        /**
         * Default constructor
         * 
         * @param idIn
         *            Идентификатор
         * @param fromWhomIn
         *            От кого
         * @param toWhomIn
         *            Кому
         * @param timeIn
         *            Время
         * @param subjectIn
         *            Тема
         */
        public SimpleOneMessage(final long idIn,
                final SimpleMessagingUser fromWhomIn,
                final SimpleMessagingUser toWhomIn, final Timestamp timeIn,
                final String subjectIn) {
            super();
            this.id = idIn;
            this.fromWhom = fromWhomIn;
            this.toWhom = toWhomIn;
            this.time = timeIn;
            this.subject = subjectIn;
        }
    }

    /**
     * Сообщение с телом
     * 
     * @author s.lezhnev
     *
     */
    public static class ExtendedSimpleOneMessage extends SimpleOneMessage {

        /**
         * Default constructor
         * 
         * @param idIn
         *            Идентификатор
         * @param fromWhomIn
         *            От кого
         * @param toWhomIn
         *            Кому
         * @param timeIn
         *            Время
         * @param subjectIn
         *            Тема
         * @param textIn
         *            Текст сообщения
         */
        public ExtendedSimpleOneMessage(final long idIn,
                final SimpleMessagingUser fromWhomIn,
                final SimpleMessagingUser toWhomIn, final Timestamp timeIn,
                final String subjectIn, final String textIn) {
            super(idIn, fromWhomIn, toWhomIn, timeIn, subjectIn);
            this.text = textIn;
        }

        /**
         * Создание из SimpleOneMessage
         * 
         * @param simpleMessage
         *            SimpleOneMessage
         * @param textIn
         *            Текст
         */
        public ExtendedSimpleOneMessage(final SimpleOneMessage simpleMessage,
                final String textIn) {
            super(simpleMessage.id, simpleMessage.fromWhom,
                    simpleMessage.toWhom, simpleMessage.time,
                    simpleMessage.subject);
            this.text = textIn;
        }

        /**
         * Сообщение
         */
        private String text;

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

    }

    /**
     * Всмопогательный класс для добавления сообщения
     * 
     * @author s.lezhnev
     *
     */
    public static class AddedSimpleOneMessage {
        /**
         * Кому
         */
        private long toWhom;
        /**
         * Тема
         */
        private String subject;
        /**
         * Тело сообщения
         */
        private String text;

        /**
         * Default constructor
         * 
         * @param toWhomIn
         *            Код пользователя - кому
         * @param subjectIn
         *            Тема
         * @param textIn
         *            Текст сообщения
         */
        public AddedSimpleOneMessage(final long toWhomIn,
                final String subjectIn, final String textIn) {
            this.toWhom = toWhomIn;
            this.subject = subjectIn;
            this.text = textIn;
        }

        /**
         * @return the toWhom
         */
        public long getToWhom() {
            return toWhom;
        }

        /**
         * @return the subject
         */
        public String getSubject() {
            return subject;
        }

        /**
         * @return the text
         */
        public String getText() {
            return text;
        }

    }

    /**
     * Вспомогательный класс - добавление в адресбук
     * 
     * @author s.lezhnev
     */
    public static class AddedToAddressBook {
        /**
         * Идентификатор пользователя
         */
        private long userId;

        /**
         * @return the userId
         */
        public long getUserId() {
            return userId;
        }

        /**
         * Default constructor
         * 
         * @param userIdIn
         *            Идентификатор пользователя
         */
        public AddedToAddressBook(final long userIdIn) {
            super();
            this.userId = userIdIn;
        }

    }

    /**
     * Вспомогательный класс - для обработки добавления юзера
     * 
     * @author s.lezhnev
     *
     */
    public static class AddedUser {
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
        private String password;

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
         */
        public AddedUser(final String firstNameIn, final String middleNameIn,
                final String lastNameIn, final String emailIn,
                final String loginIn, final String passwordIn) {
            this.firstName = firstNameIn;
            this.middleName = middleNameIn;
            this.lastName = lastNameIn;
            this.email = emailIn;
            this.login = loginIn;
            this.password = passwordIn;
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

    }

    /**
     * Конвертер из MessagingUser в SimpleMessagingUser
     * 
     * @author s.lezhnev
     */
    public static class ConvertToSimpleUser implements
            Converter<MessagingUser, SimpleMessagingUser> {

        @Override
        public SimpleMessagingUser convert(final MessagingUser source) {
            return new SimpleMessagingUser(source.getId(), source.getLogin());
        }
    }

    /**
     * Конвертер из OneMessage в SimpleOneMessage
     * 
     * @author s.lezhnev
     */
    public static class ConverToSimpleMessage implements
            Converter<OneMessage, SimpleOneMessage> {

        /**
         * Конвертер из MessagingUser в SimpleMessagingUser
         */
        private final ConvertToSimpleUser csu = new ConvertToSimpleUser();

        @Override
        public SimpleOneMessage convert(final OneMessage source) {
            return new SimpleOneMessage(source.getId(), csu.convert(source
                    .getFromWhom()), csu.convert(source.getToWhom()), source
                    .getTime(), source.getSubject());
        }

    }

    /**
     * Конвертер из OneMessage в ExtendedSimpleOneMessage
     * 
     * @author s.lezhnev
     */
    public static class ConverToExtendedSimpleMessage implements
            Converter<OneMessage, ExtendedSimpleOneMessage> {

        /**
         * Конвертер из OneMessage в SimpleOneMessage
         */
        private final ConverToSimpleMessage csm = new ConverToSimpleMessage();

        @Override
        public ExtendedSimpleOneMessage convert(final OneMessage source) {
            return new ExtendedSimpleOneMessage(csm.convert(source), source
                    .getText());
        }

    }

}
