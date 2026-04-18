package com.apiPersistence.dataPersistenceApi.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record ProfileResponseDTO(
        UUID id,
        String name,
        String gender,

        @JsonProperty("gender_probability")
        Double genderProbability,

        @JsonProperty("sample_size")
        Integer sampleSize,

        Integer age,

        @JsonProperty("age_group")
        String ageGroup,

        @JsonProperty("country_id")
        String countryId,

        @JsonProperty("country_probability")
        Double countryProbability,

        @JsonProperty("created_at")
        Instant createdAt
) {}
