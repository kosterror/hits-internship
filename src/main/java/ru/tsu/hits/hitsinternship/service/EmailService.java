package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${application.url}")
    private String applicationUrl;

    public void sendActivationLink(UserEntity user) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(user.getEmail());
        mail.setSubject("Вы зарегистрированы в системе hits-internship");
        mail.setText("Для активации аккаунта перейдите по ссылке: " + applicationUrl + "/activate/" + user.getId());
        mail.setFrom(username);

        javaMailSender.send(mail);
    }

}
