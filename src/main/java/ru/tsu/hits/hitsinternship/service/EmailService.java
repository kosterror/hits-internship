package ru.tsu.hits.hitsinternship.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.tsu.hits.hitsinternship.entity.UserEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${application.url}")
    private String applicationUrl;

    public boolean sendActivationLink(UserEntity user) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(user.getEmail());
            mail.setSubject("Регистрация в системе hits-internship");
            mail.setText(String.format("Добрый день, %s, вы были зарегистрированы деканатом в системе для поиска и " +
                                    "прохождения стажировок 'hits-internship'. " +
                                    "Для завершения регистрации и активации аккаунта перейдите по ссылке: %s/activate/%s",
                            user.getFullName(),
                            applicationUrl,
                            user.getId()
                    )
            );
            mail.setFrom(username);

            javaMailSender.send(mail);

            return true;
        } catch (Exception exception) {
            log.error("Failed send message for user {}", user.getId(), exception);
            return false;
        }
    }

}
