package com.apiPersistence.dataPersistenceApi.service;

import com.apiPersistence.dataPersistenceApi.dto.external.AgifyResponse;
import com.apiPersistence.dataPersistenceApi.dto.external.GenderizeResponse;
import com.apiPersistence.dataPersistenceApi.dto.external.NationalizeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class ExternalApiService {

    private final RestClient restClient = RestClient.create();

//    public ExternalApiService(RestClient.Builder builder) {
//        this.restClient = RestClient.builder().build();
//    }

    public GenderizeResponse fetchGender(String name) {
        return restClient.get()
                .uri("https://api.genderize.io?name={name}", name)
                .retrieve()
                .body(GenderizeResponse.class);
    }

    public AgifyResponse fetchAge(String name) {
        return restClient.get()
                .uri("https://api.agify.io?name={name}", name)
                .retrieve()
                .body(AgifyResponse.class);
    }

    public NationalizeResponse fetchNationality(String name) {
        return restClient.get()
                .uri("https://api.nationalize.io?name={name}", name)
                .retrieve()
                .body(NationalizeResponse.class);
    }

}
