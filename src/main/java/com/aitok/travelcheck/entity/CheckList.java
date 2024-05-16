package com.aitok.travelcheck.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name = "checklist")
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkListId;

    private String checkListName;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    private Country country;

    public CheckList() {
    }

    public CheckList(String checkListName, Long userId, Country country) {
        this.checkListName = checkListName;
        this.userId = userId;
        this.country = country;
    }

    public void updateCheckListName(String checkListName) {
        this.checkListName = checkListName;
    }
}
