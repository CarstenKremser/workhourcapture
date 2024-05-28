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
        AppUser appUser = userRepositiory.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found!"));
        return new User(
                appUser.username(),
                appUser.password(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public LoginUserDto getUserDtoByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepositiory.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User: " + username + " not found!"));
        return new LoginUserDto(
                appUser.id(),
                appUser.username(),
                appUser.firstName(),
                appUser.lastName()
        );
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
