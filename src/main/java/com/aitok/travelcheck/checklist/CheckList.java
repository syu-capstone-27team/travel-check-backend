package com.aitok.travelcheck.checklist;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "checklist")
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "psy_test_no")
    private int psy_test_no;
}
