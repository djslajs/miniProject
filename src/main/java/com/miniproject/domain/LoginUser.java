package com.miniproject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class LoginUser {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDateTime createAt;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "loginUser")
    private List<Session> sessions = new ArrayList<>();

    public Session addSession() {
        Session session = Session.builder()
                .loginUser( this)
                .build();
        sessions.add( session);
        return session;
    }
    @Builder
    public LoginUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createAt = LocalDateTime.now();
    }
}
