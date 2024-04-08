package com.aitok.travelcheck.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoDTO {
    private long id;
    private String email;
    private String nickname;
}