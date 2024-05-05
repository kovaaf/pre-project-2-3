package com.example.config;

import com.example.config.properties.RoleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService; // сервис, с помощью которого тащим пользователя
    private final SuccessUserHandler successUserHandler; // класс, в котором описана логика перенаправления пользователей по ролям
    private final RoleProperties roleProperties;

    public SecurityConfig(@Qualifier("appUserDetailsService") UserDetailsService userDetailsService, SuccessUserHandler successUserHandler, RoleProperties roleProperties) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
        this.roleProperties = roleProperties;
    }

    // конфигурация для прохождения аутентификации
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * Отключает генерацию специального токена, подтверждающего, что запрос отправлен с того же источника
         * csrf-атаки - это эксплоит недостатка http, при котором злоумышленник пытается заставить выполнить нежелательный запрос
         * зная все параметры запроса, который ожидает увидеть уязвимый сайт
         * при помощи незакрытой cookie сессии
         */
        // http.csrf().disable(); - попробуйте выяснить сами, что это даёт
        http.authorizeRequests()
                .antMatchers("/user/**").access(getAnyRoleString(roleProperties.getUser(), roleProperties.getAdmin())) // разрешаем входить на /user пользователям с ролью User
                .antMatchers("/admin/**").access(getAnyRoleString(roleProperties.getAdmin())) // разрешаем входить на /admin пользователям с ролью Admin
                .antMatchers("/", "/registration/**").permitAll() // доступность всем
                .anyRequest().authenticated() // все остальные страницы требуют аутентификации
            .and()
                .exceptionHandling().accessDeniedPage("/denied")
            .and()
                .formLogin()  // Spring сам подставит свою логин форму
                .successHandler(successUserHandler); // подключаем наш SuccessHandler для перенаправления по ролям
    }

    // Необходимо для шифрования паролей
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Можно переписать на свой ExpressionParser под SPeL
    // Завернуть в утилитарный класс и использовать в @PreAuthorize
    // Но это долго
    private String getAnyRoleString(String... roles) {
        return "hasAnyRole("
                + String.join(",", Arrays.stream(roles)
                                                .map(role -> "'" + role + "'")
                                                .toArray(String[]::new))
                + ")";
    }
}
