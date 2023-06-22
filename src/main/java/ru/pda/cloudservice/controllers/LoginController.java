package ru.pda.cloudservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.pda.cloudservice.components.JwtUtils;
import ru.pda.cloudservice.entitys.AuthInRequest;
import ru.pda.cloudservice.entitys.AuthToken;
import ru.pda.cloudservice.repositorys.UserRepository;

@RestController
public class LoginController {
    Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthToken authenticateUser(@RequestBody AuthInRequest authInRequest) {
//        System.out.println("authInRequest - " + authInRequest.getLogin());
        logger.info("Вход пользователя - " + authInRequest.getLogin());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (authInRequest.getLogin(),
                                authInRequest.getPassword()));

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);
        logger.info("Токен сформирован и отправлен.");
        return new AuthToken(jwt);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> doLogout(@RequestHeader("auth-token") String token) {
//        System.out.println("logout token - " + token);
        logger.info("Запрос на logout.");
        return ResponseEntity.ok("post logout");
    }

    @GetMapping("/login")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token, @RequestParam String logout) {
//        System.out.println(logout);
        String userName = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token.substring(7))).getUsername();
        logger.info("Пользователь - " + userName + ", выполнил выход.");
        return ResponseEntity.ok("get logout");
    }
}
