package com.aitok.travelcheck.checklist.repository;

import com.aitok.travelcheck.entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {
    List<CheckList> findByUserId(Long userId);
}
