package com.aitok.travelcheck.entity;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "checkitem")
public class CheckItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private boolean checked;
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "checkListId")
    private CheckList checkList;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    public CheckItem() {
    }

    public CheckItem(boolean checked, String content, CheckList checkList, Category category) {
        this.checked = checked;
        this.content = content;
        this.checkList = checkList;
        this.category = category;
    }

    // update
    public void changeCheckItem(String content, boolean checked) {
        this.content = content;
        this.checked = checked;
    }
}
