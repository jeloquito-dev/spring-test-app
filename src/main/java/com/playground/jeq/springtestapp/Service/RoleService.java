package com.playground.jeq.springtestapp.Service;

import com.playground.jeq.springtestapp.Model.Entity.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    void saveRole(Role role);
}
