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
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalDateTime confirmedAt;
    @ManyToOne
    @NonNull
    @JoinColumn(name="user_id",nullable = false)
    private User user;
}
