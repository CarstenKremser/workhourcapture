package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.LoginUserDto;
import de.carstenkremser.workhourcapture.backend.dto.RegisterUserDto;
import de.carstenkremser.workhourcapture.backend.model.AppUser;
import de.carstenkremser.workhourcapture.backend.repository.AppUserRepositiory;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserDetailsServiceTest {

    final private AppUserRepositiory mockUserRepositiory = mock(AppUserRepositiory.class);
    final private IdGenerator mockIdGenerator = mock(IdGenerator.class);
    final private PasswordEncoder mockPasswordEncoder = mock(PasswordEncoder.class);

    @Test
    void loadUserByUsername() {
        AppUserDetailsService service = new AppUserDetailsService(
                mockUserRepositiory, mockIdGenerator, mockPasswordEncoder);
        AppUser user = new AppUser(
                "id", "testuser", "testpass",
                "firstName", "lastName", "email");
        when(mockUserRepositiory.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails actual = service.loadUserByUsername("testuser");

        assertNotNull(actual);
        assertEquals("testuser", actual.getUsername());
        assertEquals("testpass", actual.getPassword());
    }

    @Test
    void getLoginUserDtoByUsername() {
        AppUserDetailsService service = new AppUserDetailsService(
                mockUserRepositiory, mockIdGenerator, mockPasswordEncoder);
        AppUser user = new AppUser(
                "id", "testuser", "testpass",
                "firstName", "lastName", "email");
        when(mockUserRepositiory.findByUsername("testuser")).thenReturn(Optional.of(user));

        LoginUserDto actual = service.getLoginUserDtoByUsername("testuser");

        assertNotNull(actual);
        assertEquals("id", actual.userid());
        assertEquals("testuser", actual.username());
        assertEquals("firstName", actual.firstname());
        assertEquals("lastName", actual.lastname());
    }

    @Test
    void saveNewUser() {
        AppUserDetailsService service = new AppUserDetailsService(
                mockUserRepositiory, mockIdGenerator, mockPasswordEncoder);
        when(mockIdGenerator.createId()).thenReturn("id4711");
        when(mockPasswordEncoder.encode("testpass")).thenReturn("testpass");

        AppUser appUser = new AppUser(
                "id4711", "testuser", "testpass",
                "firstName", "lastName", "email");

        RegisterUserDto registerUserDto = new RegisterUserDto(
                "testuser", "testpass",
                "firstName", "lastName", "email");

        service.saveNewUser(registerUserDto);

        verify(mockUserRepositiory).save(appUser);

    }
}