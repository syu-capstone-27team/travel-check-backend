package com.aitok.travelcheck.checklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CheckListService {

    @Autowired
    private CheckListRepository checkListRepository;

    public List<CheckList> getAllCheckLists() {
        return checkListRepository.findAll();
    }

    public Optional<CheckList> getCheckListById(int id) {
        return checkListRepository.findById(id);
    }

    public CheckList createOrUpdateCheckList(CheckList checkList) {
        return checkListRepository.save(checkList);
    }

    public void delteCheckListById(int id) {
        checkListRepository.deleteById(id);
    }
}
