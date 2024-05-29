package de.carstenkremser.workhourcapture.backend.dto;

public record RegisterUserDto(
        String username,
        String password,
        String firstName,
        String lastName,
        String email
) {
}
