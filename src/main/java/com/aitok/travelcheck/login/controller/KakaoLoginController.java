package com.aitok.travelcheck.login.controller;

import com.aitok.travelcheck.common.MsgEntity;
import com.aitok.travelcheck.login.dto.KakaoDTO;
import com.aitok.travelcheck.login.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin()); // 카카오로그인 경로
        return "login";
    }

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code")); // 토큰받기

        return ResponseEntity.ok()
                .body(new MsgEntity("Success", kakaoInfo));
    }

}
