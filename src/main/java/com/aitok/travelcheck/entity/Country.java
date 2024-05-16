package com.aitok.travelcheck.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "country")
public class Country {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long countryId;

    private String countryName;

}
