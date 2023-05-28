package com.libaitu.libaitu.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class EmailRegistrationConfirmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false,unique = true)
    @NonNull
    private String token;
    @Column(nullable = false)
    @NonNull
    private LocalDateTime createdAt;
    @Column(nullable = false)
    @NonNull
    private LocalDateTime expiresAt;

    @Column(nullable = false,unique = true)
    @NonNull
    private String email;
}