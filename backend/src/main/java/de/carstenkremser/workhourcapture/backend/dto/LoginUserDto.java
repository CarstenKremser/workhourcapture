package de.carstenkremser.workhourcapture.backend.dto;

public record LoginUserDto(
        String username,
        String firstName,
        String lastName
) {
}
