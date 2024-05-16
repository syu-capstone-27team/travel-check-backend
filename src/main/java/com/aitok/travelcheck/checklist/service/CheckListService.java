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
        Optional<CheckList> optionalCheckList = checkListRepository.findById(requestDTO.getCheckListId());

        Country country = countryRepository.findById(requestDTO.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        CheckList checkList = checkListConverter.convertToEntity(requestDTO, country); // dto -> entity 변환
        checkListRepository.save(checkList); // insert쿼리 2회수행 (checkList)

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


}
