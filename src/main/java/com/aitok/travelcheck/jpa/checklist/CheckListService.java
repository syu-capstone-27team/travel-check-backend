package com.aitok.travelcheck.jpa.checklist;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CheckListService {

    @Autowired
    private CheckListRepository checkListRepository;

    public CheckList saveCheckList(CheckList checkList) {
        return checkListRepository.save(checkList);
    }

    public List<CheckList> findAllCheckLists() {
        return checkListRepository.findAll();
    }

    public Optional<CheckList> findCheckListById(Long id) {
        return checkListRepository.findById(id);
    }

    public CheckList updateCheckList(Long id, CheckList checkListDetails) {
        CheckList checkList = checkListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CheckList not found with id " + id));
        checkList.setUser(checkListDetails.getUser());
        checkList.setCountry(checkListDetails.getCountry());
        return checkListRepository.save(checkList);
    }

    public void deleteCheckList(Long id) {
        CheckList checkList = checkListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CheckList not found with id " + id));
        checkListRepository.delete(checkList);
    }
}
