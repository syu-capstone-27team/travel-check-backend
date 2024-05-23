package com.aitok.travelcheck.checklist.service;


import com.aitok.travelcheck.checklist.repository.CategoryRepository;
import com.aitok.travelcheck.checklist.repository.CheckItemRepository;
import com.aitok.travelcheck.checklist.repository.CheckListRepository;
import com.aitok.travelcheck.checklist.repository.CountryRepository;
import com.aitok.travelcheck.common.CheckListConverter;
import com.aitok.travelcheck.dto.request.CheckItemRequestDTO;
import com.aitok.travelcheck.dto.request.CheckListRequestDTO;
import com.aitok.travelcheck.dto.response.CheckListResponseDTO;
import com.aitok.travelcheck.entity.Category;
import com.aitok.travelcheck.entity.CheckItem;
import com.aitok.travelcheck.entity.CheckList;
import com.aitok.travelcheck.entity.Country;
import com.aitok.travelcheck.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Entity는 조회용을 인식하고, 변경감지(dirty check)를 위한 스냅샷을 보관X
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final CheckItemRepository checkItemRepository;
    private final CountryRepository countryRepository;
    private final CategoryRepository categoryRepository;
    private final CheckListConverter checkListConverter;

    public List<CheckListResponseDTO> getAllCheckLists(Long userId) {
        System.out.println("=======");
        List<CheckList> checkLists = checkListRepository.findByUserId(userId);
        // spring data jpa는 메소드 이름(findByUserId)를 분석해 쿼리를 짬
        // find : 조회 /By : 조건의 시작 / UserId : 엔티티 속성 이름

        System.out.println(checkLists.get(0).getCheckListId());

//        List<CheckListResponseDTO> collect = checkLists.stream().map(checkListConverter::convertToDTO).collect(Collectors.toList());

        return checkLists.stream().map(checkListConverter::convertToDTO).collect(Collectors.toList()); // 체크리스트를 ENTITY가 아닌 DTO로 변환(보안 OR 확장성)

    }

    @Transactional // 갱신
    public CheckListResponseDTO saveCheckList(CheckListRequestDTO requestDTO, long userId) {
//        System.out.println("requestDTO = " + requestDTO.getCheckListId());
//        Optional<CheckList> optionalCheckList = checkListRepository.findById(requestDTO.getCheckListId());

        Country country = countryRepository.findById(requestDTO.getCountryId())
                .orElseThrow(() -> new RuntimeException("나라가 찾아지지 않음"));

        CheckList checkList = checkListConverter.convertToEntity(requestDTO, country); // dto -> entity 변환
        checkListRepository.save(checkList); // insert쿼리 수행 (checkList)

        // 여러 체크항목(N)이 있을 경우, 그 때마다 select쿼리를 통해 카테고리를 가져오는 것은 비효율적이니 미리 가져오자
        List<Long> categoryIds = requestDTO.getCheckItems().stream()
                .map(CheckItemRequestDTO::getCateId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Category> categoryMap = categoryRepository.findByCategoryIdIn(categoryIds)
                .stream().collect(Collectors.toMap(Category::getCategoryId, category -> category));


        List<CheckItem> checkItems = requestDTO.getCheckItems().stream() // 1. DTO에서 ItemRequestDTO 리스트 호출해서 스트림으로 변환
                .map(itemDTO -> checkListConverter.convertToEntity(itemDTO, checkList, categoryMap)) // ItemRequestDTO를 convertToEntity 메소드로 checkItem으로 변환한다.
                .collect(Collectors.toList());


        checkItemRepository.saveAll(checkItems);
        return checkListConverter.convertToDTO(checkList, checkItems);
    }

    @Transactional
    public boolean deleteCheckLists(Long checkListId, Long userId){
        // 자기꺼만 수정하게 추가해야함
        // 체크리스트와 관련된 체크아이템 먼저 삭제
        Optional<CheckList> optionalCheckList = checkListRepository.findById(checkListId);

        if (optionalCheckList.isPresent()) {
            CheckList checkList = optionalCheckList.get();
            if (checkList.getUserId().equals(userId)) {
                List<CheckItem> checkItems = checkItemRepository.findByCheckList_CheckListId(checkListId);
                checkItemRepository.deleteAll(checkItems);

                // checkList 삭제
                checkListRepository.deleteById(checkListId);
                return true;
            }
        }

        return false;
    }
    @Transactional
    public boolean deleteCheckListItemsBatch(Long checkListId, List<Long> itemIds, Long userId) {
        // 체크리스트를 조회하고 사용자 검증
        CheckList checkList = checkListRepository.findById(checkListId)
                .filter(cl -> cl.getUserId().equals(userId))
                .orElseThrow(() -> new UnauthorizedException("잘못된 회원 혹은 체크리스트가 찾아지지 않음"));

        // 삭제할 체크리스트 항목들을 조회하고 체크리스트 ID 검증
        List<CheckItem> checkItems = checkItemRepository.findAllById(itemIds);
        // 전자 : 없는 번호를 입력하면 checkItems.size() = 0 / -> 없는 번호
        // 후자 :
        if (checkItems.size() != itemIds.size() || checkItems.stream().anyMatch(item -> !item.getCheckList().getCheckListId().equals(checkListId))) {
            throw new IllegalArgumentException("하나 이상의 항목이 체크리스트에 속하지 않거나 존재하지 않습니다");
        }

        // 체크리스트 항목 일괄 삭제
        checkItemRepository.deleteAll(checkItems);
        return true;
    }
}


