package com.apiPersistence.dataPersistenceApi.service;

import com.apiPersistence.dataPersistenceApi.dto.external.AgifyResponse;
import com.apiPersistence.dataPersistenceApi.dto.external.GenderizeResponse;
import com.apiPersistence.dataPersistenceApi.dto.external.NationalizeResponse;
import com.apiPersistence.dataPersistenceApi.exception.ExternalApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class ExternalApiService {

    private final RestClient restClient = RestClient.create();

    public GenderizeResponse fetchGender(String name) {
        try {
            return restClient.get()
                    .uri("https://api.genderize.io?name={name}", name)
                    .retrieve()
                    .body(GenderizeResponse.class);
        } catch (Exception e) {
            throw new ExternalApiException("Genderize");
        }
    }

    public AgifyResponse fetchAge(String name) {
        try {
            return restClient.get()
                    .uri("https://api.agify.io?name={name}", name)
                    .retrieve()
                    .body(AgifyResponse.class);
        } catch (Exception e) {
            throw new ExternalApiException("Agify");
        }
    }

    public NationalizeResponse fetchNationality(String name) {
        try {
            return restClient.get()
                    .uri("https://api.nationalize.io?name={name}", name)
                    .retrieve()
                    .body(NationalizeResponse.class);
        } catch (Exception e) {
            throw new ExternalApiException("Nationalize");
        }
    }
}