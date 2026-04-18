package com.apiPersistence.dataPersistenceApi.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record NationalizeResponse(String name,
                                  List<CountryEntry> country) {
    public record CountryEntry(
            @JsonProperty("country_id") String countryId,
            Double probability){}
}
