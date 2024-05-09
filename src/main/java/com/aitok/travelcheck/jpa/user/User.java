package com.aitok.travelcheck.jpa.user;

import com.aitok.travelcheck.jpa.checklist.CheckList;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String nickName;

    @OneToMany(mappedBy = "user")
    private List<CheckList> checkLists;
}