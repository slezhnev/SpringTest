package ru.lsv.messaging;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.lsv.messaging.dao.MessagingUser;
import ru.lsv.messaging.dao.MessagingUserRepository;

/**
 * Загрузка пользователей
 * 
 * @author s.lezhnev
 */
@Service
@Qualifier("messagingUserDetailsService")
public class MessagingUserDetailsService implements UserDetailsService {

    /**
     * Репо доступа к пользователям
     */
    @Autowired
    private MessagingUserRepository usrRep;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.core.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {
        MessagingUser usr = usrRep.findByLogin(username);
        if ((usr == null) || (usr.getRoles() == null) ||
                (usr.getRoles().size() == 0)) {
            throw new UsernameNotFoundException("Login \"" + username +
                    " \" not found");
        }

        return new User(usr.getLogin(), usr.getPassword(), usr.getRoles()
                .stream().map(role -> {
                    return new SimpleGrantedAuthority(role.getRole());
                }).collect(Collectors.toList()));
    }

}
