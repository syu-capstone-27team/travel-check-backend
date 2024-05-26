package com.aitok.travelcheck.checklist.controller;

import com.aitok.travelcheck.checklist.service.CheckListService;
import com.aitok.travelcheck.common.MsgEntity;
import com.aitok.travelcheck.dto.request.CheckItemRequestDTO;
import com.aitok.travelcheck.dto.request.CheckListRequestDTO;
import com.aitok.travelcheck.dto.response.CheckListResponseDTO;
import com.aitok.travelcheck.entity.CheckItem;
import com.aitok.travelcheck.entity.CheckList;
import com.aitok.travelcheck.exception.UnauthorizedException;
import com.aitok.travelcheck.login.dto.KakaoDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checklists")
public class CheckListController {

    private final CheckListService checkListService;

    /**
     * 로그인한 유저의 모든 체크리스트 조회
     * @param session : 회원 정보 저장.
     * @return : "회원"의 모든 체크리스트 조회
     */
    @GetMapping
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
     * insert
     * @param requestDTO 체크리스트 입력 파라미터(체크리스트 + 체크아이템)
     * @param session 회원 정보
     * @return 저장한 체크리스트 + 각종 ID
     */
    @PostMapping
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



    /**
     * 체크리스트 + 체크리스트 항목 제거 메소드
     * @param checkListId
     * @param session
     * @return 200
     */
    @DeleteMapping
    public ResponseEntity<MsgEntity> deleteCheckLists(@RequestParam("id") Long checkListId, HttpSession session) {
        KakaoDTO kakaoDTO = (KakaoDTO) session.getAttribute("kakaoDTO");

        // 세션 검사
        if (kakaoDTO == null || kakaoDTO.getAccessToken() == null) {
            throw new UnauthorizedException("No access token found in session.");
        }

        Long userId = kakaoDTO.getId();
        boolean isDeleted = checkListService.deleteCheckLists(checkListId, userId);

        if (isDeleted) {
            return new ResponseEntity<>(new MsgEntity("CheckList successfully deleted", null), HttpStatus.OK); // 200 성공
        } else {
            return new ResponseEntity<>(new MsgEntity("Forbidden: Unable to delete CheckList", null), HttpStatus.FORBIDDEN); // 403 권한 거부
        }
    }

    /**
     * 체크리스트 항목 제거 메소드
     * @param checkListId
     * @param itemIds
     * @param session
     * @return
     */
    @DeleteMapping("/{checklistId}/items")
    public ResponseEntity<MsgEntity> deleteCheckListItem(@PathVariable("checklistId") Long checkListId,
                                                         @RequestBody List<Long> itemIds,
                                                         HttpSession session) {
        KakaoDTO kakaoDTO = (KakaoDTO) session.getAttribute("kakaoDTO");

        // 세션 검사
        if (kakaoDTO == null || kakaoDTO.getAccessToken() == null) {
            throw new UnauthorizedException("No access token found in session.");
        }

        Long userId = kakaoDTO.getId();
        boolean isDeleted = checkListService.deleteCheckListItemsBatch(checkListId, itemIds, userId);

        if (isDeleted) {
            return new ResponseEntity<>(new MsgEntity("CheckListItem successfully deleted", null), HttpStatus.OK); // 200 성공
        } else {
            return new ResponseEntity<>(new MsgEntity("Forbidden: Unable to delete CheckListItem", null), HttpStatus.FORBIDDEN); // 403 권한 거부
        }
    }

    /**
     * 체크리스트 수정 메소드
     * @param checkListId
     * @param requestDTO
     * @param session
     * @return 수정된 체크리스트
     */
    @PutMapping("/{checkListId}")
    public ResponseEntity<MsgEntity> updateCheckList(@PathVariable("checkListId") Long checkListId,
                                                     @RequestBody CheckListRequestDTO requestDTO,
                                                     HttpSession session) {
        KakaoDTO kakaoDTO = (KakaoDTO) session.getAttribute("kakaoDTO");

        // 세션 검사
        if (kakaoDTO == null || kakaoDTO.getAccessToken() == null) {
            throw new UnauthorizedException("No access token found in session.");
        }

        Long userId = kakaoDTO.getId();
        CheckListResponseDTO responseDTO = checkListService.updateCheckList(checkListId, requestDTO, userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new MsgEntity("Success", responseDTO));
    }

    /**
     * 체크리스트 항목 수정 메소드
     * @param checkListId
     * @param itemId
     * @param requestDTO
     * @param session
     * @return 수정된 체크리스트 항목
     */
    @PutMapping("/{checkListId}/items/{itemId}")
    public ResponseEntity<MsgEntity> updateCheckListItem(@PathVariable("checkListId") Long checkListId,
                                                         @PathVariable("itemId") Long itemId,
                                                         @RequestBody CheckItemRequestDTO requestDTO,
                                                         HttpSession session) {
        KakaoDTO kakaoDTO = (KakaoDTO) session.getAttribute("kakaoDTO");

        // 세션 검사
        if (kakaoDTO == null || kakaoDTO.getAccessToken() == null) {
            throw new UnauthorizedException("No access token found in session.");
        }

        Long userId = kakaoDTO.getId();
        boolean isUpdated = checkListService.updateCheckListItem(checkListId, itemId, requestDTO, userId);

        if (isUpdated) {
            return new ResponseEntity<>(new MsgEntity("항목 업데이트 성공", null), HttpStatus.OK); // 200 성공
        } else {
            return new ResponseEntity<>(new MsgEntity("Forbidden: Unable to update CheckListItem", null), HttpStatus.FORBIDDEN); // 403 권한 거부
        }
    }


}
