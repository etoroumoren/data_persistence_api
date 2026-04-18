package com.apiPersistence.dataPersistenceApi.repository;

import com.apiPersistence.dataPersistenceApi.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository <Profile, UUID> {

    Optional<Profile> findByNameIgnoreCase(String name);

}
