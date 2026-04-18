package com.apiPersistence.dataPersistenceApi.exception;

import java.util.UUID;

public class ProfileNotFoundException extends RuntimeException{

    public ProfileNotFoundException(UUID id) {
        super("Profile not found with id: " + id);
    }
}
