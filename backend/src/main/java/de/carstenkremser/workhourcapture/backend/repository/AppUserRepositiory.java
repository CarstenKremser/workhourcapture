package de.carstenkremser.workhourcapture.backend.repository;

import de.carstenkremser.workhourcapture.backend.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppUserRepositiory extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);
}
