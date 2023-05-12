package com.libaitu.libaitu.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class TokenRefreshResponse {
    @NonNull
    private String accessToken;
    @NonNull
    private String refreshToken;
    private String type="Bearer";
}
