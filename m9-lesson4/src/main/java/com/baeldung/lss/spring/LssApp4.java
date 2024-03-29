package com.baeldung.lss.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan({ "com.baeldung.lss.web", "com.baeldung.lss.spring" })
@EnableJpaRepositories("com.baeldung.lss")
@EntityScan("com.baeldung.lss.web.model")
public class LssApp4 {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Class[] { LssApp4.class, LssSecurityConfig.class, LssWebMvcConfiguration.class }, args);
    }

}
