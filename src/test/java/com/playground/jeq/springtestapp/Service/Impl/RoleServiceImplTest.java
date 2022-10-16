package com.playground.jeq.springtestapp.Service.Impl;

import com.playground.jeq.springtestapp.Model.Entity.Role;
import com.playground.jeq.springtestapp.Repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock private RoleRepository roleRepository;

    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    void canSaveRole() {
        //Given
        Role role = new Role(1L, "ROLE_ADMIN");

        //When
        roleService.saveRole(role);

        //Then
        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(roleArgumentCaptor.capture());
        Role capturedRole = roleArgumentCaptor.getValue();

        assertThat(capturedRole).isEqualTo(role);
    }
}