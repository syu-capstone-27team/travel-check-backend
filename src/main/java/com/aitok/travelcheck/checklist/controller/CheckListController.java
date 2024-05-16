package com.aitok.travelcheck.checklist.controller;

import com.aitok.travelcheck.checklist.service.CheckListService;
import com.aitok.travelcheck.common.MsgEntity;
import com.aitok.travelcheck.dto.request.CheckListRequestDTO;
import com.aitok.travelcheck.dto.response.CheckListResponseDTO;
import com.aitok.travelcheck.entity.CheckList;
import com.aitok.travelcheck.exception.UnauthorizedException;
import com.aitok.travelcheck.login.dto.KakaoDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CheckListController {

    private final CheckListService checkListService;

    /**
     * 로그인한 유저의 모든 체크리스트 조회
     * @param session : 회원 정보 저장.
     * @return : "회원"의 모든 체크리스트 조회
     */
    @GetMapping("/checklists")
    public ResponseEntity<MsgEntity> getAllCheckLists(HttpSession session) {

        KakaoDTO kakaoDTO = (KakaoDTO) session.getAttribute("kakaoDTO"); // 세션에서 회원 정보를 꺼낸다
        if (kakaoDTO != null && kakaoDTO.getAccessToken() != null) {
            long id = kakaoDTO.getId();
            List<CheckListResponseDTO> allCheckLists = checkListService.getAllCheckLists(id);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MsgEntity("Success", allCheckLists));

        } else {
            throw new UnauthorizedException("No access token found in session.");
        }
    }


    /**
     * insert or Update 기능
     * @param requestDTO 체크리스트 입력 파라미터(체크리스트 + 체크아이템)
     * @param session 회원 정보
     * @return 저장한 체크리스트 + 각종 ID
     */
    @PostMapping("/checklists") //
    public ResponseEntity<MsgEntity> saveCheckList(@RequestBody CheckListRequestDTO requestDTO, HttpSession session) {
        KakaoDTO kakaoDTO = (KakaoDTO) session.getAttribute("kakaoDTO");

        // 회원 존재 여부
        if (kakaoDTO != null && kakaoDTO.getAccessToken() != null) {
            long userId = kakaoDTO.getId();
            CheckListResponseDTO responseDTO = checkListService.saveCheckList(requestDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MsgEntity("Success", responseDTO));
        } else {
            throw new UnauthorizedException("No access token found in session.");
        }
    }


}