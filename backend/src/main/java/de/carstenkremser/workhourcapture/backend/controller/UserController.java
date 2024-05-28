package de.carstenkremser.workhourcapture.backend.controller;

import de.carstenkremser.workhourcapture.backend.dto.LoginUserDto;
import de.carstenkremser.workhourcapture.backend.dto.RegisterUserDto;
import de.carstenkremser.workhourcapture.backend.service.AppUserDetailsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final AppUserDetailsService detailsService;

    @GetMapping("/me")
    public String getCurrentUser() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @PostMapping("/login")
    public LoginUserDto login() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return detailsService.getUserDtoByUsername(username);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterUserDto userDto) {

        detailsService.saveNewUser(userDto);
    }
}
