package com.aitok.travelcheck.common;

import com.aitok.travelcheck.dto.request.CheckItemRequestDTO;
import com.aitok.travelcheck.dto.request.CheckListRequestDTO;
import com.aitok.travelcheck.dto.response.CheckItemResponseDTO;
import com.aitok.travelcheck.dto.response.CheckListResponseDTO;
import com.aitok.travelcheck.entity.Category;
import com.aitok.travelcheck.entity.CheckItem;
import com.aitok.travelcheck.entity.CheckList;
import com.aitok.travelcheck.entity.Country;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CheckListConverter {

    // CheckList -> CheckListResponseDTO
    public CheckListResponseDTO convertToDTO(CheckList checkList) {
        return CheckListResponseDTO.builder()
                .checkListId(checkList.getCheckListId())
                .checkListName(checkList.getCheckListName())
                .userId(checkList.getUserId())
                .countryId(checkList.getCountry().getCountryId())
                .build();
    }

    // CheckList -> CheckListResponseDTO with CheckItems
    public CheckListResponseDTO convertToDTO(CheckList checkList, List<CheckItem> checkItems) {
        List<CheckItemResponseDTO> items = checkItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return CheckListResponseDTO.builder()
                .checkListId(checkList.getCheckListId())
                .countryId(checkList.getCountry().getCountryId())
                .checkListName(checkList.getCheckListName())
                .userId(checkList.getUserId())
                .checkItems(items)
                .build();
    }

    // CheckItem -> CheckItemResponseDTO
    public CheckItemResponseDTO convertToDTO(CheckItem checkItem) {
        return CheckItemResponseDTO.builder()
                .itemId(checkItem.getItemId())
                .content(checkItem.getContent())
                .cateId(checkItem.getCategory().getCategoryId())
                .checked(checkItem.isChecked())
                .build();
    }

    // CheckListRequestDTO -> CheckList
    public CheckList convertToEntity(CheckListRequestDTO requestDTO, Country country) {
        return new CheckList(requestDTO.getCheckListName(), requestDTO.getUserId(), country);
    }

    // CheckItemRequestDTO -> CheckItem
    public CheckItem convertToEntity(CheckItemRequestDTO requestDTO, CheckList checkList, Map<Long, Category> categoryMap) {
        Category category = categoryMap.get(requestDTO.getCateId());
        if (category == null) {
            throw new RuntimeException("Category not found");
        }

        return new CheckItem(requestDTO.isChecked(), requestDTO.getContent(), checkList, category);
    }
}