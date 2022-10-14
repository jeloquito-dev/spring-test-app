package com.playground.jeq.springtestapp.Service.Impl;

import com.playground.jeq.springtestapp.Model.Role;
import com.playground.jeq.springtestapp.Repository.RoleRepository;
import com.playground.jeq.springtestapp.Service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }
}
