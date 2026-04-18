package com.apiPersistence.dataPersistenceApi.dto.external;

import java.util.UUID;

public record ProfileSummaryDTO(UUID id,
                                String name,
                                String gender,
                                Integer age,
                                String ageGroup,
                                String countryId) {
}
