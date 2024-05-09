package com.aitok.travelcheck.jpa.checklist;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "checkitem")
public class CheckItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private boolean checked;
    private String content;

    @ManyToOne
    @JoinColumn(name = "checkListId")
    private CheckList checkList;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

}
