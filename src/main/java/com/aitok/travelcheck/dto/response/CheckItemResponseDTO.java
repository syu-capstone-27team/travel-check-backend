package com.aitok.travelcheck.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckItemResponseDTO {

    private Long itemId;
    private String content;
    private Boolean checked;
    private Long cateId;

}
