package com.baeldung.lss.spring;

import com.baeldung.lss.security.LockedUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@EnableWebSecurity
@Configuration
public class LssSecurityConfig {

    private final PasswordEncoder passwordEncoder;

    public LssSecurityConfig(final PasswordEncoder passwordEncoder) {
        super();
        this.passwordEncoder = passwordEncoder;
    }

    //

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception { // @formatter:off
        auth.
                inMemoryAuthentication().passwordEncoder(passwordEncoder)
                .withUser("user").password(passwordEncoder.encode("pass")).roles("USER").and()
                .withUser("admin").password(passwordEncoder.encode("pass")).roles("ADMIN")
        ;
    } // @formatter:on

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {// @formatter:off
        http
                .authorizeRequests()
                .antMatchers("/secured").access("hasRole('ADMIN')")
                .anyRequest().authenticated().accessDecisionManager(unanimous())

                .and()
                .formLogin().
                loginPage("/login").permitAll().
                loginProcessingUrl("/doLogin")

                .and()
                .logout().permitAll().logoutUrl("/logout")

                .and()
                .csrf().disable();
        return http.build();
    }

    @Bean
    public AccessDecisionManager unanimous() {
        final List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList(
                new RoleVoter(), new AuthenticatedVoter(), new RealTimeLockVoter(), new WebExpressionVoter());
        return new UnanimousBased(voters);
    }

    class RealTimeLockVoter implements AccessDecisionVoter<Object> {

        @Override
        public boolean supports(final ConfigAttribute attribute) {
            return false;
        }

        @Override
        public boolean supports(final Class<?> clazz) {
            return false;
        }

        @Override
        public int vote(final Authentication authentication, final Object object, final Collection<ConfigAttribute> attributes) {
            if (LockedUsers.isLocked(authentication.getName())) {
                return ACCESS_DENIED;
            }
            return ACCESS_GRANTED;
        }
    }
}
