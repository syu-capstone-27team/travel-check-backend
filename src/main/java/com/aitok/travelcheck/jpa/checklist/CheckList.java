package com.aitok.travelcheck.jpa.checklist;

import com.aitok.travelcheck.jpa.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "checklist")
public class CheckList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkListId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "countryId")
    private Country country;

    @OneToMany(mappedBy = "checkList")
    private List<CheckItem> checkItems;
}
