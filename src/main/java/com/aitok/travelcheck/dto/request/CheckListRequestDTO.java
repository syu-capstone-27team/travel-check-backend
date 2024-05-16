package com.aitok.travelcheck.dto.request;

import com.aitok.travelcheck.entity.CheckItem;
import com.aitok.travelcheck.entity.CheckList;
import com.aitok.travelcheck.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckListRequestDTO {
    private Long checkListId;
    private String checkListName;
    private Long countryId;
    private Long userId;
    private List<CheckItemRequestDTO> checkItems;


}
