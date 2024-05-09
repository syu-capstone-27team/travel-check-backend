package com.aitok.travelcheck.login.controller;

import com.aitok.travelcheck.common.MsgEntity;
import com.aitok.travelcheck.login.dto.KakaoDTO;
import com.aitok.travelcheck.login.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request, HttpSession session) throws Exception {
        String code = request.getParameter("code"); // 토큰 받기
        KakaoDTO kakaoInfo = kakaoService.getKakaoInfo(code); // 토큰 전송

        if (kakaoInfo != null) {
            session.setAttribute("loginMember", kakaoInfo);
            session.setMaxInactiveInterval(60 * 30);
        }

        return ResponseEntity.ok()
                .body(new MsgEntity("Success", kakaoInfo));
    }

    // logout
    @GetMapping("/logout")
    public String kakaoLogout(HttpSession session) {
        KakaoDTO member = (KakaoDTO) session.getAttribute("loginMember");
        String accessToken = member.getAccessToken();
        System.out.println(accessToken);
        if (accessToken != null && !"".equals(accessToken)) {
            try {
                kakaoService.kakaoDisconnect(accessToken);
            } catch (Exception e) {
                System.err.println("Failed to logout from Kakao: " + e.getMessage());
                return "errorPage"; // 예외 상황을 처리하는 뷰 페이지로 리다이렉트
            }
            session.removeAttribute("loginMember");
        } else {
            System.out.println("accessToken is null");
        }

        System.out.println("=====================");
        return "redirect:/login";
    }


}
