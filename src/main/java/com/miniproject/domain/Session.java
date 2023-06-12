package com.miniproject.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED)
@Getter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne
    private LoginUser loginUser;

    @Builder
    public Session(LoginUser loginUser) {
        this.accessToken = UUID.randomUUID().toString();
        this.loginUser = loginUser;
    }
}
