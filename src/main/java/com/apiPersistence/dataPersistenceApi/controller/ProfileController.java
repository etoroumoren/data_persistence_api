package com.apiPersistence.dataPersistenceApi.controller;


import com.apiPersistence.dataPersistenceApi.dto.external.ProfileSummaryDTO;
import com.apiPersistence.dataPersistenceApi.entity.Profile;
import com.apiPersistence.dataPersistenceApi.mapper.ProfileMapper;
import com.apiPersistence.dataPersistenceApi.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;
    private final ProfileMapper mapper;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> body) {
        Object nameVal = body.get("name");

        // 422 if name key exists but is wrong type
        if (nameVal != null && !(nameVal instanceof String)) {
            return ResponseEntity.unprocessableEntity()
                    .body(Map.of("status", "error", "message", "name must be a string"));
        }

        String name = nameVal instanceof String s ? s : null;

        // Check if profile already exists
        Optional<Profile> existing = service.findExisting(name);
        if (existing.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Profile already exists",
                    "data", mapper.toResponseDTO(existing.get())
            ));
        }

        Profile created = service.createProfile(name);
        return ResponseEntity.status(201)
                .body(Map.of("status", "success", "data", mapper.toResponseDTO(created)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Profile profile = service.getById(id);
        return ResponseEntity.ok(Map.of("status", "success",
                "data", mapper.toResponseDTO(profile)));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String country_id,
            @RequestParam(required = false) String age_group
    ) {
        List<ProfileSummaryDTO> results = service.getAll(gender, country_id, age_group)
                .stream().map(mapper::toSummaryDTO).toList();

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", results.size(),
                "data", results
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
