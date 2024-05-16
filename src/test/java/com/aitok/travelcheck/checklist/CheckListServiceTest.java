package com.aitok.travelcheck.checklist;

import com.aitok.travelcheck.checklist.repository.CheckListRepository;
import com.aitok.travelcheck.checklist.repository.CountryRepository;
import com.aitok.travelcheck.entity.CheckList;
import com.aitok.travelcheck.entity.Country;
import com.aitok.travelcheck.login.dto.KakaoDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class CheckListTest {

    @Autowired
    CheckListRepository checkListRepository;

    @Autowired
    CountryRepository countryRepository;

    private MockHttpSession session;

    /**
     * 테스트 실행 전 초기화 작업
     */
    @BeforeEach
    public void setup() {
        KakaoDTO kakaoDTO = KakaoDTO.builder()
                .id(1L)
                .email("test@example.com")
                .nickname("testuser")
                .accessToken("valid-token")
                .build();

        session = new MockHttpSession();
        session.setAttribute("kakaoDTO", kakaoDTO);

//        Country country1 = new Country("korea");
//        Country country2 = new Country("japan");
        countryRepository.save(country1);
        countryRepository.save(country2);

//        // 초기화 데이터
//        CheckList checkList1 = new CheckList("Test CheckList 1", 1L, country1);
//        CheckList checkList2 = new CheckList("Test CheckList 2", 1L, country2);


        checkListRepository.save(checkList1);
        checkListRepository.save(checkList2);
    }

    @Test
    @Transactional
    @DisplayName("회원에 맞는 체크리스트 조회하기")
    public void findCheckListByUserId() {
        // 세션에서 KakaoDTO 가져오기
        KakaoDTO kakaoDTO = (KakaoDTO) session.getAttribute("kakaoDTO");
        Long userId = kakaoDTO.getId();

        // userId로 체크리스트 조회
        List<CheckList> checkLists = checkListRepository.findByUserId(userId);

        // 검증
        assertThat(checkLists).hasSize(2);
        assertThat(checkLists.get(0).getCheckListName()).isEqualTo("Test CheckList 1");
        assertThat(checkLists.get(0).getCountry().getCountryName()).isEqualTo("korea");
        assertThat(checkLists.get(1).getCheckListName()).isEqualTo("Test CheckList 2");
        assertThat(checkLists.get(1).getCountry().getCountryName()).isEqualTo("japan");
    }

}