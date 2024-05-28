package de.carstenkremser.workhourcapture.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Users")
public record AppUser(
        @Id
        String id,
        String username,
        String password,
        String firstName,
        String lastName
) {
}
