package com.aitok.travelcheck.checklist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "CheckList API", description = "체크리스트 API 입니다.")
@RestController
@RequestMapping("/api/v1/checklist")
public class CheckListController {

    @Autowired
    private CheckListService checkListService;

    @Tag(name = "CheckList API")
    @Operation(summary = "체크리스트 리스트 조회", description = "체크리스트 리스트를 조회합니다.")
    @GetMapping("/")
    public List<CheckList> getAllCheckLists() {
        return checkListService.getAllCheckLists();
    }
}
