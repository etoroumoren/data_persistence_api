package com.apiPersistence.dataPersistenceApi.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "profiles")
@NoArgsConstructor
public class Profile {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;

    private String gender;
    private Double genderProbability;
    private Integer sampleSize;
    private Integer age;
    private String ageGroup;
    private String countryId;
    private Double countryProbability;

    @Column(nullable = false)
    private Instant createdAt;
}
