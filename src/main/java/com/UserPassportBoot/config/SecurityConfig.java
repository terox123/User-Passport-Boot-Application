package com.UserPassportBoot.config;

import com.UserPassportBoot.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public SecurityConfig(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/auth/login", "/auth/registration",
                                "/css/**", "/js/**", "/images/**" ).permitAll()
                        .requestMatchers("/").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/users/**","/passports/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                /*
                зарегистрироваться могут все пользователи, даже не существующие
                к страничке home имеют доступ авторизованные пользователи с ролями user, admin
                к странице управления паспортами и пользователями имеют доступ только админы
                 */
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error")
                        .permitAll()
                )
         /* страница логина, при удачной авторизации, пользователя отправляюь на home страницу,
          иначе остаётся на логине
           */
                .logout(log -> log
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                )

                .userDetailsService(myUserDetailsService);

        return http.build();
    }
}