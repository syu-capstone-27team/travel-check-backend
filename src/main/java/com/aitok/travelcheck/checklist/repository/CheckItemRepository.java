package com.aitok.travelcheck.checklist.repository;

import com.aitok.travelcheck.entity.CheckItem;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckItemRepository extends JpaRepository<CheckItem, Long> {
    List<CheckItem> findByCheckList_CheckListId(Long checkListId);
}
