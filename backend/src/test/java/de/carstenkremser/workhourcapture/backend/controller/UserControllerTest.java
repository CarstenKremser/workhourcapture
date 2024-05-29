package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.RegisterUserDto;
import de.carstenkremser.workhourcapture.backend.service.AppUserDetailsService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UserControllerTest {

    final private AppUserDetailsService mockUserDetailsService = mock(AppUserDetailsService.class);

    @Test
    void register() {
        RegisterUserDto userDto = new RegisterUserDto (
                "username", "password",
                "firstName", "lastName", "email");
        UserController controller = new UserController(mockUserDetailsService);

        controller.register(userDto);

        verify(mockUserDetailsService).saveNewUser(userDto);
    }
}