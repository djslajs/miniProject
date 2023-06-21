package com.miniproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity(debug = true) //debug true는 운영서버에서는 사용x
public class SecurityConfig{

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers( "/error")
                .requestMatchers(toH2Console());
//        return new WebSecurityCustomizer() {
//            @Override
//            public void customize(WebSecurity web) {
//                web.ignoring().requestMatchers("/favicon.ico", "/error")
////                .requestMatchers( new AntPathRequestMatcher("/h2-console/**"));
//                .requestMatchers( toH2Console());
//            }
//        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                    .requestMatchers("/auth/login").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .usernameParameter( "username")
                    .passwordParameter( "password")
                    .loginPage( "/auth/login")
                    .loginProcessingUrl("/auth/login")
                    .defaultSuccessUrl("/")
                .and()
                .userDetailsService( userDetailsService())
                .csrf(AbstractHttpConfigurer::disable) // csrf가 무엇은지 체크해보기
                .build();
    }

    @Bean // 로그인 정보 체크
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        UserDetails user = User
                .withUsername("cho")
                .password("1234")
                .roles("ADMIN")
                .build();
        manager.createUser( user);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
