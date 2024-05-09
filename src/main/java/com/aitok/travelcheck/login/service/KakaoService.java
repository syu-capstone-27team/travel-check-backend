package com.aitok.travelcheck.login.service;

import com.aitok.travelcheck.login.dto.KakaoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@Service
public class KakaoService {

    @Value("${kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.client_secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${kakao.redirect_uri}")
    private String KAKAO_REDIRECT_URL;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com"; // 인증 코드 주소
    private final static String KAKAO_API_URI = "https://kapi.kakao.com";

    public String getKakaoLogin() { // 카카오 로그인 경로 설정
        return KAKAO_AUTH_URI + "/oauth/authorize"
                + "?client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URL
                + "&response_type=code";
    }

    public KakaoDTO getKakaoInfo(String code) throws Exception {
        if (code == null) throw new Exception("Failed get authorization code"); // 토큰이 없으면 예외처리

        String accessToken = "";
        String refreshToken = "";

        try {
            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded");

            // HTTP 요청에 필요한 파라미터 설정
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type"   , "authorization_code");
            params.add("client_id"    , KAKAO_CLIENT_ID);
            params.add("client_secret", KAKAO_CLIENT_SECRET);
            params.add("code"         , code);
            params.add("redirect_uri" , KAKAO_REDIRECT_URL);

            RestTemplate restTemplate = new RestTemplate();
            // HTTP 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            // restAPI를 호출하고 받는 응답
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_AUTH_URI + "/oauth/token",
                    HttpMethod.POST, // POST 메소드로 요청
                    httpEntity, // 요청 엔티티
                    String.class // 응답 데이터 형식
            );


            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> tokenMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, String>>() {});

            accessToken = tokenMap.get("access_token");
            refreshToken = tokenMap.get("refresh_token");


        } catch (Exception e) {
            throw new Exception("API call failed");
        }

        return getUserInfoWithToken(accessToken);
    }

    private KakaoDTO getUserInfoWithToken(String accessToken) throws Exception {
        //HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 담기
        RestTemplate rt = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = rt.exchange(
                KAKAO_API_URI + "/v2/user/me",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        //Response 데이터 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());

        long id = rootNode.get("id").longValue();
        String email = rootNode.path("kakao_account").path("email").asText(null); // 이메일이 없을 경우 null 반환
        String nickname = rootNode.path("kakao_account").path("profile").path("nickname").asText();

        return KakaoDTO.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .build();
    }

    public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 콘텐트 타입 설정
        headers.setBearerAuth(accessToken); // 액세스 토큰 설정

        // HTTP 요청 객체 생성, 본문은 비어 있으나 Content-Type 설정 필요
        HttpEntity<String> kakaoLogoutRequest = new HttpEntity<>("", headers);
        RestTemplate rt = new RestTemplate();
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());


        System.out.println("KakaoService.kakaoDisconnect1");

        try {
            // HTTP 요청 보내기, 응답 받기
            ResponseEntity<String> response = rt.exchange("https://kapi.kakao.com/v1/user/logout",
                    HttpMethod.POST,
                    kakaoLogoutRequest,
                    String.class
            );

            System.out.println("KakaoService.kakaoDisconnect2");

            // 응답 본문에서 JSON 데이터 추출
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // 반환된 id를 로그로 출력, 응답에 id가 포함되어 있는지 확인 필요
            if (jsonNode.has("id")) {
                Long id = jsonNode.get("id").asLong();
                System.out.println("Returned id: " + id);
            } else {
                System.out.println("No 'id' field in response body");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // HTTP 요청 에러 처리: 에러 로깅 또는 추가적인 예외 처리
            System.err.println("HTTP request failed: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            throw e; // 필요에 따라 예외를 다시 던질 수 있습니다.
        }
    }
}
