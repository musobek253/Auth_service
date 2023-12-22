package com.musobek.auth_service.entity;

import com.musobek.auth_service.entity.enam.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    @Enumerated(value = EnumType.STRING)
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user-id")
    private User user;

}
