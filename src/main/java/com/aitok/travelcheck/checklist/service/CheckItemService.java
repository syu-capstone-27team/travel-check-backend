package com.aitok.travelcheck.checklist.service;

import com.aitok.travelcheck.checklist.repository.CheckItemRepository;
import com.aitok.travelcheck.checklist.repository.CheckListRepository;
import com.aitok.travelcheck.common.CheckListConverter;
import com.aitok.travelcheck.dto.response.CheckItemResponseDTO;
import com.aitok.travelcheck.entity.CheckItem;
import com.aitok.travelcheck.entity.CheckList;
import com.aitok.travelcheck.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckItemService {

    private final CheckItemRepository checkItemRepository;
    private final CheckListConverter checkListConverter;
    private final CheckListRepository checkListRepository;

    public List<CheckItemResponseDTO> getCheckItemsByCheckListId(Long checkListId, Long userId) {
        CheckList checkList = checkListRepository.findById(checkListId)
                .orElseThrow(() -> new RuntimeException("CheckList not found"));

        // 소유자 확인
        if (!checkList.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view this checklist.");
        }

        List<CheckItem> checkItems = checkItemRepository.findByCheckList_CheckListId(checkListId);
        return checkItems.stream()
                .map(checkListConverter::convertToDTO)
                .collect(Collectors.toList());
    }

}
