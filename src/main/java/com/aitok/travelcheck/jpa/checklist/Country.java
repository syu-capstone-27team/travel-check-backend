package com.aitok.travelcheck.jpa.checklist;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countryId;

    private String countryName;

    @OneToMany(mappedBy = "country")
    private List<CheckList> checkLists;

}
