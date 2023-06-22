package ru.pda.cloudservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pda.cloudservice.entitys.AuthUser;
import ru.pda.cloudservice.repositorys.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AuthUser myUser= userRepository.findByUsername(userName);
        if (myUser == null) {
            logger.info("Неизвестный пользователь: " + userName);
            throw new UsernameNotFoundException("Неизвестный пользователь: " + userName);
        }
        UserDetails user = User.builder()
                .username(myUser.getUsername())
                .password("{noop}" + myUser.getPassword())
                .roles(myUser.getRole().toUpperCase())
                .build();
        logger.info("Идентификатор пользователя: " + myUser.getId());
        return user;
    }
}
