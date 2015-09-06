package ru.lsv.messaging.dao;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Сообщение
 * 
 * @author s.lezhnev
 */
@Entity
@Table(name = "messages")
public class OneMessage {

    /**
     * Идетификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * От кого
     */
    @ManyToOne(targetEntity = MessagingUser.class)
    private MessagingUser fromWhom;
    /**
     * Кому
     */
    @ManyToOne(targetEntity = MessagingUser.class)
    private MessagingUser toWhom;
    /**
     * Время
     */
    private Timestamp time;
    /**
     * Тема
     */
    private String subject;
    /**
     * Тело сообщения
     */
    @Lob
    private String text;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @return the fromWhom
     */
    public MessagingUser getFromWhom() {
        return fromWhom;
    }

    /**
     * @return the toWhom
     */
    public MessagingUser getToWhom() {
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
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * For JPA
     */
    protected OneMessage() {

    }

    /**
     * Default constructor
     * 
     * @param fromWhomIn
     *            От кого
     * @param toWhomIn
     *            Кому
     * @param timeIn
     *            Время
     * @param subjectIn
     *            Тема
     * @param textIn
     *            Тело сообщения
     */
    public OneMessage(final MessagingUser fromWhomIn,
            final MessagingUser toWhomIn, final Timestamp timeIn,
            final String subjectIn, final String textIn) {
        this.fromWhom = fromWhomIn;
        this.toWhom = toWhomIn;
        this.time = timeIn;
        this.subject = subjectIn;
        this.text = textIn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String
                .format("OneMessage [fromWhom=%s, toWhom=%s, time=%s, subject=%s, text=%s]",
                        fromWhom, toWhom, time, subject, text);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof OneMessage) {
            return this.id == ((OneMessage) obj).id;
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
