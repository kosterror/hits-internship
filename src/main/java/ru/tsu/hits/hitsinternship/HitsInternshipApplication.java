package ru.tsu.hits.hitsinternship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@ConfigurationPropertiesScan("ru.tsu.hits.hitsinternship")
@EnableMethodSecurity
public class HitsInternshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(HitsInternshipApplication.class, args);
    }

}
