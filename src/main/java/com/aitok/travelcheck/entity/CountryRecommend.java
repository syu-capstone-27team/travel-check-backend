package com.aitok.travelcheck.entity;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class CountryRecommend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "countryId")
    private Country country;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    private String recItemName;

}
