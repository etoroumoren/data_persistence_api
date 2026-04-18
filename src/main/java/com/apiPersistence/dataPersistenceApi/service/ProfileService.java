package com.apiPersistence.dataPersistenceApi.service;

import com.apiPersistence.dataPersistenceApi.dto.external.AgifyResponse;
import com.apiPersistence.dataPersistenceApi.dto.external.GenderizeResponse;
import com.apiPersistence.dataPersistenceApi.dto.external.NationalizeResponse;
import com.apiPersistence.dataPersistenceApi.exception.ExternalApiException;

import com.apiPersistence.dataPersistenceApi.entity.Profile;
import com.apiPersistence.dataPersistenceApi.exception.InvalidRequestException;
import com.apiPersistence.dataPersistenceApi.exception.ProfileNotFoundException;
import com.apiPersistence.dataPersistenceApi.repository.ProfileRepository;
import com.github.f4b6a3.uuid.UuidCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repo;
    private final ExternalApiService externalApi;

    public Profile createProfile(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("Name is required");
        }

        Optional<Profile> existing = repo.findByNameIgnoreCase(name.trim());
        if (existing.isPresent()) {
            return existing.get();
        }

        CompletableFuture<GenderizeResponse> genderFuture =
                CompletableFuture.supplyAsync(() -> externalApi.fetchGender(name));

        CompletableFuture<AgifyResponse> ageFuture =
                CompletableFuture.supplyAsync(() -> externalApi.fetchAge(name));

        CompletableFuture<NationalizeResponse> nationalFuture =
                CompletableFuture.supplyAsync(() -> externalApi.fetchNationality(name));

        CompletableFuture.allOf(genderFuture, ageFuture, nationalFuture).join();

        GenderizeResponse genderData = genderFuture.join();
        AgifyResponse ageData = ageFuture.join();
        NationalizeResponse nationalData = nationalFuture.join();

        if (genderData.gender() == null || genderData.count() == 0) {
            throw new ExternalApiException("Genderize");
        }
        if (ageData.age() == null) {
            throw new ExternalApiException("Agify");
        }
        if (nationalData.country() == null || nationalData.country().isEmpty()) {
            throw new ExternalApiException("Nationalize");
        }

        // 5. Classify
        String ageGroup = ProfileClassifier.classifyAge(ageData.age());
        var topCountry = ProfileClassifier.topCountry(nationalData.country());

        // 6. Build and save
        Profile profile = new Profile();
        profile.setId(UuidCreator.getTimeOrderedEpoch());
        profile.setName(name.trim().toLowerCase());
        profile.setGender(genderData.gender());
        profile.setGenderProbability(genderData.probability());
        profile.setSampleSize(genderData.count());
        profile.setAge(ageData.age());
        profile.setAgeGroup(ageGroup);
        profile.setCountryId(topCountry.countryId());
        profile.setCountryProbability(topCountry.probability());
        profile.setCreatedAt(Instant.now());

        return repo.save(profile);
    }

    public Profile getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException(id));
    }

    public List<Profile> getAll(String gender, String countryId, String ageGroup) {
        return repo.findAll().stream()
                .filter(p -> gender == null || (p.getGender() != null && p.getGender().equalsIgnoreCase(gender)))
                .filter(p -> countryId == null || (p.getCountryId() != null && p.getCountryId().equalsIgnoreCase(countryId)))
                .filter(p -> ageGroup == null || (p.getAgeGroup() != null && p.getAgeGroup().equalsIgnoreCase(ageGroup)))
                .toList();
    }

    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ProfileNotFoundException(id);
        }
        repo.deleteById(id);
    }

    public Optional<Profile> findExisting(String name) {
        if (name == null || name.isBlank()) return Optional.empty();
        return repo.findByNameIgnoreCase(name.trim());
    }
}
