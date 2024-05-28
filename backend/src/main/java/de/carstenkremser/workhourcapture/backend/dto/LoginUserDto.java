package de.carstenkremser.workhourcapture.backend.dto;

public record LoginUserDto(
        String userid,
        String username,
        String firstname,
        String lastname
) {
}
