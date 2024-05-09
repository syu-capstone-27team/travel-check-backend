package com.aitok.travelcheck.jpa.checklist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "CheckList API", description = "체크리스트 API 입니다.")
@RestController
@RequestMapping("/api/v1/checklist")
public class CheckListController {

    @Autowired
    private CheckListService checkListService;

//    @GetMapping("/")
//    public List<CheckList> getAllCheckLists() {
//        return checkListService.getAllCheckLists();
//    }

    @Tag(name = "CheckList API")
    @Operation(summary = "체크리스트 리스트 생성", description = "체크리스트를 생성합니다.")
    @PostMapping
    public ResponseEntity<CheckList> createCheckList(@RequestBody CheckList checkList) {
        return ResponseEntity.ok(checkListService.saveCheckList(checkList));
    }

    @GetMapping("/")
    public ResponseEntity<List<CheckList>> getAllCheckLists() {
        return ResponseEntity.ok(checkListService.findAllCheckLists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckList> getCheckListById(@PathVariable Long id) {
        return checkListService.findCheckListById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckList> updateCheckList(@PathVariable Long id, @RequestBody CheckList checkListDetails) {
        return ResponseEntity.ok(checkListService.updateCheckList(id, checkListDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckList(@PathVariable Long id) {
        checkListService.deleteCheckList(id);
        return ResponseEntity.ok().build();
    }
}