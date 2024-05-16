package com.aitok.travelcheck.dto.response;

import com.aitok.travelcheck.entity.CheckItem;
import com.aitok.travelcheck.entity.CheckList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CheckListResponseDTO {
    private Long checkListId;
    private String checkListName;
    private Long countryId;
    private Long userId;
    private List<CheckItemResponseDTO> checkItems;

}