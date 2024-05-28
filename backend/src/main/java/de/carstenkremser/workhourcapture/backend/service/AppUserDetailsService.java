package de.carstenkremser.workhourcapture.backend.service;

import de.carstenkremser.workhourcapture.backend.dto.LoginUserDto;
import de.carstenkremser.workhourcapture.backend.dto.RegisterUserDto;
import de.carstenkremser.workhourcapture.backend.model.AppUser;
import de.carstenkremser.workhourcapture.backend.repository.AppUserRepositiory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepositiory userRepositiory;
    private final IdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // >>> für development:
        if (username.equals("defaultUser")) {
            return new User(
                    "defaultUser",
                    passwordEncoder.encode("test"),
                    //"$argon2i$v=19$m=16,t=2,p=1$YXNkZnF3ZXJ0eg$boCWOOuJl1LI1c1PUA1g5A", // password="test", salt= "asdfqwertz"
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        // <<<
        AppUser appUser = userRepositiory.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found!"));
        return new User(
                appUser.username(),
                appUser.password(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
    }

    public LoginUserDto getUserDtoByUsername(String username) {
        try {
            AppUser appUser = userRepositiory.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found!"));
            return new LoginUserDto(
                    appUser.username(),
                    appUser.firstName(),
                    appUser.lastName()
            );
        } catch (UsernameNotFoundException _e) {
            return new LoginUserDto(username, "", "");
        }
    }

    public void saveNewUser(RegisterUserDto userDto) {
        AppUser user = new AppUser(
                idGenerator.createId(),
                userDto.username(),
                passwordEncoder.encode(userDto.password()),
                userDto.firstName(),
                userDto.lastName(),
                userDto.email()
        );
        userRepositiory.save(user);
    }
}
