package com.apiPersistence.dataPersistenceApi.dto.external;

import java.time.Instant;
import java.util.UUID;

public record ProfileResponseDTO(UUID id,
                                 String name,
                                 String gender,
                                 Double genderProbability,
                                 Integer sampleSize,
                                 Integer age,
                                 String ageGroup,
                                 String countryId,
                                 Double countryProbability,
                                 Instant createdAt) {
}
