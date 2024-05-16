package com.aitok.travelcheck.checklist;

import com.aitok.travelcheck.checklist.controller.CheckListController;
import com.aitok.travelcheck.checklist.service.CheckListService;
import com.aitok.travelcheck.dto.request.CheckListRequestDTO;
import com.aitok.travelcheck.dto.response.CheckListResponseDTO;
import com.aitok.travelcheck.login.dto.KakaoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckListController.class) // CheckListController를 대상으로 하는 MVC 테스트를 설정합니다.
public class CheckListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // CheckListService를 모킹합니다.
    private CheckListService checkListService;

    private MockHttpSession session; // MockHttpSession 객체를 선언합니다.

    @BeforeEach // 각 테스트 메서드 실행 전에 실행되는 메서드를 설정합니다.
    public void setup() {
        // KakaoDTO 객체를 빌드하여 세션에 설정합니다.
        KakaoDTO kakaoDTO = KakaoDTO.builder()
                .id(1L) // 사용자 ID를 설정합니다.
                .email("test@example.com")
                .nickname("testuser") // 닉네임을 설정합니다.
                .accessToken("valid-token") // 액세스 토큰을 설정합니다.
                .build(); // KakaoDTO 객체를 빌드합니다.

        session = new MockHttpSession(); // MockHttpSession 객체를 초기화합니다.
        session.setAttribute("kakaoDTO", kakaoDTO); // 세션에 KakaoDTO 객체를 설정합니다.
    }

    @Test
    public void testGetAllCheckLists_WithValidToken() throws Exception {
        // CheckListDTO 객체 빌드
//        CheckListRequestDTO checkList1 = CheckListRequestDTO.builder()
                .checkListId(1L)
                .checkListName("Test CheckList 1")
                .userId(1L)
                .countryName("korea")
                .build();

//        CheckListRequestDTO checkList2 = CheckListRequestDTO.builder()
                .checkListId(2L)
                .checkListName("Test CheckList 2")
                .userId(1L)
                .countryName("japan")
                .build();

        List<CheckListResponseDTO> checkLists = Arrays.asList(checkList1, checkList2); // 두 개의 CheckListDTO 객체를 리스트로 만듭니다.

        // checkListService.getAllCheckLists() 메서드가 호출되면 checkLists 리스트를 반환하도록 모킹합니다.
        Mockito.when(checkListService.getAllCheckLists(anyLong())).thenReturn(checkLists);

        // MockMvc를 사용하여 GET 요청을 수행하고 결과를 검증합니다.
        mockMvc.perform(get("/checklists") // "/checklists" 경로로 GET 요청을 보냅니다.
                        .session(session) // 세션을 설정합니다.
                        .contentType(MediaType.APPLICATION_JSON)) // 요청의 Content-Type을 JSON으로 설정합니다.
                .andExpect(status().isOk()) // 응답 상태가 200 OK인지 검증합니다.
                .andExpect(jsonPath("$[0].checkListName").value("Test CheckList 1")) // 첫 번째 체크리스트 이름이 "Test CheckList 1"인지 검증합니다.
                .andExpect(jsonPath("$[0].countryName").value("korea")) // 첫 번째 체크리스트의 국가 이름이 "korea"인지 검증합니다.
                .andExpect(jsonPath("$[1].checkListName").value("Test CheckList 2")) // 두 번째 체크리스트 이름이 "Test CheckList 2"인지 검증합니다.
                .andExpect(jsonPath("$[1].countryName").value("japan")); // 두 번째 체크리스트의 국가 이름이 "japan"인지 검증합니다.
    }

    @Test // 테스트 메서드를 지정합니다.
    public void testGetAllCheckLists_WithoutToken() throws Exception {
        session.removeAttribute("kakaoDTO"); // 세션에서 KakaoDTO 객체를 제거합니다.

        // MockMvc를 사용하여 GET 요청을 수행하고 결과를 검증합니다.
        mockMvc.perform(get("/checklists") // "/checklists" 경로로 GET 요청을 보냅니다.
                        .session(session) // 세션을 설정합니다.
                        .contentType(MediaType.APPLICATION_JSON)) // 요청의 Content-Type을 JSON으로 설정합니다.
                .andExpect(status().isUnauthorized()); // 응답 상태가 401 Unauthorized인지 검증합니다.
    }
}

