package com.apiPersistence.dataPersistenceApi.mapper;

import com.apiPersistence.dataPersistenceApi.dto.external.ProfileResponseDTO;
import com.apiPersistence.dataPersistenceApi.dto.external.ProfileSummaryDTO;
import com.apiPersistence.dataPersistenceApi.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponseDTO toResponseDTO(Profile p) {
        return new ProfileResponseDTO(
                p.getId(), p.getName(), p.getGender(),
                p.getGenderProbability(), p.getSampleSize(),
                p.getAge(), p.getAgeGroup(), p.getCountryId(),
                p.getCountryProbability(), p.getCreatedAt()
        );
    }

    public ProfileSummaryDTO toSummaryDTO(Profile p) {
        return new ProfileSummaryDTO(
                p.getId(), p.getName(), p.getGender(),
                p.getAge(), p.getAgeGroup(), p.getCountryId()
        );
    }
}
