package ru.lsv.messaging;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.lsv.messaging.SimpleViews.AddedUser;
import ru.lsv.messaging.SimpleViews.ConvertToSimpleUser;
import ru.lsv.messaging.SimpleViews.ExtendedSimpleOneMessage;
import ru.lsv.messaging.SimpleViews.SimpleMessagingUser;
import ru.lsv.messaging.dao.MessagingUser;
import ru.lsv.messaging.dao.MessagingUserRepository;
import ru.lsv.messaging.dao.MessagingUserRole;
import ru.lsv.messaging.dao.OneMessage;
import ru.lsv.messaging.dao.OneMessageRepository;

/**
 * Контроллер для сообщений
 * 
 * @author s.lezhnev
 */
@RestController
public final class MessagingController {

    /**
     * Конвертер OneMessage в SimpleOneMessage
     */
    private final SimpleViews.ConverToSimpleMessage msgConverter =
            new SimpleViews.ConverToSimpleMessage();
    /**
     * Конвертер OneMessage в ExtendedSimpleOneMessage
     */
    private final SimpleViews.ConverToExtendedSimpleMessage extMsgConverter =
            new SimpleViews.ConverToExtendedSimpleMessage();
    /**
     * Конвертер MessagingUser в SimpleMessagingUser
     */
    private final SimpleViews.ConvertToSimpleUser usrConverter =
            new ConvertToSimpleUser();
    /**
     * Репо для сообщений
     */
    private final OneMessageRepository msgRep;
    /**
     * Репо для юзеров
     */
    private final MessagingUserRepository usrRep;

    /**
     * Default constructor
     * 
     * @param msgRepIn
     *            Репо для сообщений
     * @param usrRepIn
     *            Репо для юзеров
     */
    @Autowired
    public MessagingController(final OneMessageRepository msgRepIn,
            final MessagingUserRepository usrRepIn) {
        this.msgRep = msgRepIn;
        this.usrRep = usrRepIn;
    }

    /**
     * Получение одного сообщения
     * 
     * @param messageId
     *            Идентификатор сообщения
     * @param authentication
     *            Идентификация пользователя
     * @return Сообщение
     */
    @RequestMapping(value = "/messages/{messageId}", method = RequestMethod.GET)
    public ExtendedSimpleOneMessage getMessage(
            @PathVariable final long messageId,
            final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            // Send empty page
            return null;
        }
        OneMessage msg = msgRep.findOne(messageId);
        if (msg != null) {
            return extMsgConverter.convert(msg);
        } else {
            return null;
        }
    }

    /**
     * Удаление сообщения
     * 
     * @param messageId
     *            Идентификатор сообщения
     * @param authentication
     *            Идентификация текущего пользователя
     * @return Результат удаления
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/messages/{messageId}/delete")
    public ResponseEntity deleteMessage(@PathVariable final long messageId,
            final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        // Поехали удалять
        MessagingUser currUser = usrRep.findByLogin(authentication.getName());
        if (currUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        OneMessage currMsg = msgRep.findOne(messageId);
        if (currMsg == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Удалить сообщение можно только если это админ
        // или текущий пользователь является либо автором, либо сообщение
        // адресовано ему
        if ((authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(
                        MessagingUserRole.Roles.ADMIN.toString()))) ||
                currUser.equals(currMsg.getFromWhom()) ||
                currUser.equals(currMsg.getToWhom())) {
            msgRep.delete(currMsg);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // Удалять нельзя
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Получение сообщений
     * 
     * @param page
     *            Страница
     * @param authentication
     *            Идентификация пользователя
     * @return Сообщения
     */
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public Page<SimpleViews.SimpleOneMessage> getAllMessages(
            @PageableDefault(sort = { "time" }, direction = Sort.Direction.DESC, value = 10) final Pageable page,
            final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            // Send empty page
            return new PageImpl<>(new ArrayList<>());
        }
        if (authentication.getAuthorities().contains(
                new SimpleGrantedAuthority(MessagingUserRole.Roles.ADMIN
                        .toString()))) {
            // Return ALL messages
            return msgRep.findAll(page).map(msgConverter);
        }
        MessagingUser usr = usrRep.findByLogin(authentication.getName());
        if (usr != null) {
            return msgRep.findByToWhomOrFromWhom(usr, usr, page).map(
                    msgConverter);
        } else {
            return null;
        }
    }

    /**
     * Добавление сообщения
     * 
     * @param addedMessage
     *            Добавляемое сообщение
     * @param bindingResult
     *            Binding results
     * @param authentication
     *            Идентификация текущего пользователя
     * @return Результат обработки
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public ResponseEntity addMessage(
            @Valid final SimpleViews.AddedSimpleOneMessage addedMessage,
            final BindingResult bindingResult,
            final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Поехали сохранять
        MessagingUser currUser = usrRep.findByLogin(authentication.getName());
        MessagingUser toUser = usrRep.findOne(addedMessage.getToWhom());
        if ((currUser == null) && (toUser == null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        OneMessage newMessage =
                new OneMessage(currUser, toUser, new Timestamp(
                        new java.util.Date().getTime()), addedMessage
                        .getSubject(), addedMessage.getText());
        msgRep.save(newMessage);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}").buildAndExpand(
                        newMessage.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
    }

    /**
     * Удаление пользователя
     * 
     * @param userId
     *            Код юзера для удаления
     * @param authentication
     *            Идентификация текущего пользователя
     * @return Результат удаления
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/users/{userId}/delete")
    public ResponseEntity removeUser(@PathVariable final long userId,
            final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            // Send empty page
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!authentication.getAuthorities().contains(
                new SimpleGrantedAuthority(MessagingUserRole.Roles.ADMIN
                        .toString()))) {
            // Удалять пользователей может только админ
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        MessagingUser currUser = usrRep.findOne(userId);
        if (currUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        usrRep.delete(currUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 
     * @param user
     *            Пользователь
     * @param bindingResult
     *            Результаты биндинга
     * @return Результат добавления (обратно на форму добавления или ссылка на
     *         login)
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String addNewUser(@Valid final AddedUser user,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // TODO: Допилить выдачу сообщения об ошибке
            return "/register";
        }
        // Добавляем
        MessagingUser newUser =
                new MessagingUser(user.getFirstName(), user.getMiddleName(),
                        user.getLastName(), user.getEmail(), user.getLogin(),
                        PasswordCrypto.getInstance()
                                .encrypt(user.getPassword()), null);
        newUser.setRoles(Stream.of(
                new MessagingUserRole(MessagingUserRole.Roles.USER.toString(),
                        newUser)).collect(Collectors.toSet()));
        usrRep.save(newUser);
        return "/";
    }

    /**
     * Получение списка пользователей (доступно только для администраторов)
     * 
     * @param page
     *            Страница
     * @param authentication
     *            Идентификация пользователя
     * @return Список пользователей
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Page<MessagingUser> getUsers(
            @PageableDefault(sort = { "time" }, direction = Sort.Direction.DESC, value = 10) final Pageable page,
            final Authentication authentication) {
        if (authentication.isAuthenticated() &&
                authentication.getAuthorities().contains(
                        new SimpleGrantedAuthority(
                                MessagingUserRole.Roles.ADMIN.toString()))) {
            return usrRep.findAll(page);
        } else {
            return new PageImpl<>(new ArrayList<>());
        }
    }

    /**
     * Удаление элемента из адресной книге
     * 
     * @param userId
     *            Код юзера для удаления
     * @param authentication
     *            Идентификация пользователя
     * @return Результат удаления
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/addressbook/{userId}/delete")
    public ResponseEntity removeFromAddressBook(
            @PathVariable final long userId, final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        // Поехали удалять
        MessagingUser currUser = usrRep.findByLogin(authentication.getName());
        MessagingUser delUser = usrRep.findOne(userId);
        if ((currUser == null) || (delUser == null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!currUser.getAddressBook().contains(delUser)) {
            // Если такого пользователя нет в адресбуке - ругаемся...
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        currUser.getAddressBook().remove(delUser);
        usrRep.save(currUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Добавление записи в адресную книгу
     * 
     * @param userId
     *            Пользователь
     * @param bindingResult
     *            Результат биндинга
     * @param authentication
     *            Идентификация текущего пользователя
     * @return Результат добавления
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/addressbook", method = RequestMethod.POST)
    public ResponseEntity addToAddressBook(
            @Valid final SimpleViews.AddedToAddressBook userId,
            final BindingResult bindingResult,
            final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Поехали сохранять
        MessagingUser currUser = usrRep.findByLogin(authentication.getName());
        MessagingUser addingUser = usrRep.findOne(userId.getUserId());
        if ((currUser == null) && (addingUser == null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (currUser.getAddressBook().contains(addingUser)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        currUser.getAddressBook().add(addingUser);
        usrRep.save(currUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Получение адресной книги для текущего пользователя
     * 
     * @param authentication
     *            Идентификация пользоватеоя
     * @return Адресная книга
     */
    @RequestMapping(value = "/addressbook", method = RequestMethod.GET)
    public List<SimpleMessagingUser> getAddressBook(
            final Authentication authentication) {
        if (!authentication.isAuthenticated()) {
            // Send empty page
            return new ArrayList<>();
        }
        MessagingUser usr = usrRep.findByLogin(authentication.getName());
        if (usr != null) {
            return usr.getAddressBook().parallelStream().map(user -> {
                return usrConverter.convert(usr);
            }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

}
