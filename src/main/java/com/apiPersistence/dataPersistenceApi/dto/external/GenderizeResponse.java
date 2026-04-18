package com.apiPersistence.dataPersistenceApi.dto.external;

public record GenderizeResponse(String name,
                                String gender,
                                Double probability,
                                Integer count) {
}
