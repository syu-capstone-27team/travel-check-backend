package com.aitok.travelcheck.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckItemRequestDTO {

    private String content;
    private Long cateId;
    private boolean checked;
}
