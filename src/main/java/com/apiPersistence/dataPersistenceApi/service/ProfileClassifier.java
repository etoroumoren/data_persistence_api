package com.apiPersistence.dataPersistenceApi.service;

import com.apiPersistence.dataPersistenceApi.dto.external.NationalizeResponse;
import com.apiPersistence.dataPersistenceApi.exception.ExternalApiException;

import java.util.Comparator;
import java.util.List;

public class ProfileClassifier {

    public static String classifyAge(int age) {
        if (age <= 12) return "child";
        if (age <= 19) return "teenager";
        if (age <= 59) return "adult";
        return "senior";
    }

    public static NationalizeResponse.CountryEntry topCountry(
            List<NationalizeResponse.CountryEntry> countries) {
        return countries.stream()
                .max(Comparator.comparingDouble(
                        NationalizeResponse.CountryEntry::probability))
                .orElseThrow(() ->
                        new ExternalApiException("Nationalize"));
    }
}
