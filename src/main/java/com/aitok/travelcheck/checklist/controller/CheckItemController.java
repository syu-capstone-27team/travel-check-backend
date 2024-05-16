package com.aitok.travelcheck.checklist.controller;

import com.aitok.travelcheck.checklist.service.CheckItemService;
import com.aitok.travelcheck.common.MsgEntity;
import com.aitok.travelcheck.dto.response.CheckItemResponseDTO;
import com.aitok.travelcheck.dto.response.CheckListResponseDTO;
import com.aitok.travelcheck.exception.UnauthorizedException;
import com.aitok.travelcheck.login.dto.KakaoDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CheckItemController {

    private final CheckItemService checkItemService;

    @GetMapping("/checklists/{id}")
    public ResponseEntity<MsgEntity> getCheckItems(@PathVariable("id") Long checkListId, HttpSession session) {
        KakaoDTO kakaoDTO = (KakaoDTO) session.getAttribute("kakaoDTO");

        // 회원 존재 여부
        if (kakaoDTO != null && kakaoDTO.getAccessToken() != null) {
            long userId = kakaoDTO.getId();
            List<CheckItemResponseDTO> responseDTO = checkItemService.getCheckItemsByCheckListId(checkListId, userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new MsgEntity("Success", responseDTO));
        } else {
            throw new UnauthorizedException("No access token found in session.");
        }
    }

}
