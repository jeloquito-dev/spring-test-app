package com.playground.jeq.springtestapp.Service.Impl;

import com.playground.jeq.springtestapp.Model.Entity.AppUser;
import com.playground.jeq.springtestapp.Model.Entity.Role;
import com.playground.jeq.springtestapp.Repository.AppUserRepository;
import com.playground.jeq.springtestapp.Repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock private AppUserRepository appUserRepository;
    @Mock private RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    private AppUserServiceImpl appUserService;

    @BeforeEach
    void setUp() {
        appUserService = new AppUserServiceImpl(appUserRepository, roleRepository, passwordEncoder);
    }
    @Test
    void canSaveAppUser() {
        //Given
        Role role = new Role(1L, "ROLE_ADMIN");
        String encodedPassword = passwordEncoder.encode("password");
        AppUser appUser = new AppUser(1L, "Test User", "test", "password", List.of(role));

        //When
        appUserService.saveAppUser(appUser);

        //Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(capturedAppUser).isEqualTo(appUser);
    }

    @Test
    void canAddRoleToUser() {
        //Given
        Role role = new Role(1L, "ROLE_ADMIN");
        AppUser appUser = new AppUser(1L, "Test User", "test", "password", new ArrayList<>());
        given(roleRepository.findByDescription(role.getDescription())).willReturn(role);
        given(appUserRepository.findByUsername(appUser.getUsername())).willReturn(appUser);

        //When
        appUserService.addRoleToAppUser(appUser.getUsername(), role.getDescription());

        //Then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getRoles()).isEqualTo(List.of(role));
    }

    @Test
    void canGetAppUserById() {
        //Given
        Long id = 1L;
        AppUser appUser = new AppUser(1L, "Test User", "test", "password", new ArrayList<>());
        given(appUserRepository.getOne(id)).willReturn(appUser);

        //When
        AppUser result = appUserService.getAppUserById(id);

        //Then
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(appUserRepository).getOne(longArgumentCaptor.capture());
        Long capturedLong = longArgumentCaptor.getValue();

        assertThat(capturedLong).isEqualTo(id);
        assertThat(result).isEqualTo(appUser);
    }

    @Test
    void canFindAppUserByUsername() {
        //Given
        String username = "test";
        AppUser appUser = new AppUser(1L, "Test User", username, "password", new ArrayList<>());
        given(appUserRepository.findByUsername(username)).willReturn(appUser);

        //When
        AppUser result = appUserService.findAppUserByUsername(username);

        //Then
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(appUserRepository).findByUsername(stringArgumentCaptor.capture());
        String capturedString = stringArgumentCaptor.getValue();

        assertThat(capturedString).isEqualTo(username);
        assertThat(result).isEqualTo(appUser);
    }

    @Test
    void canGetAllUser() {
        //When
        appUserService.getAllUser();

        //Then
        verify(appUserRepository).findAll();
    }

    @Test
    void canLoadUserByUsername() {
        //Given
        String username = "test";
        String password = "password";
        Role role = new Role(1L, "ROLE_ADMIN");
        AppUser appUser = new AppUser(1L, "Test User", username, password, List.of(role));
        given(appUserRepository.findByUsername(username)).willReturn(appUser);

        //When
        UserDetails userDetails = appUserService.loadUserByUsername(username);

        //Then
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    @Test
    void canThrowUsernameNotFoundExceptionOnLoadUserByUsername() {
        //Given
        given(appUserRepository.findByUsername(anyString())).willReturn(null);

        //Then
        assertThatThrownBy(() -> appUserService.loadUserByUsername(anyString()))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}