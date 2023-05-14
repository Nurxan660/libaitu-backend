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
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer refreshTokenId;
    @NonNull
    @OneToOne
    @JoinColumn(nullable = false,unique = true,name="user_id")
    private User user;
    @NonNull
    @Column(nullable = false,unique = true)
    private String token;
    @NonNull
    @Column(nullable = false)
    private LocalDateTime expiredDate;
}
