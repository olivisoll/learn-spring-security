package com.baeldung.lss.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class LssSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    public LssSecurityConfig(final PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().
                passwordEncoder(passwordEncoder)
                .withUser("user").password(passwordEncoder.encode("pass"))
                .roles("USER");
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
