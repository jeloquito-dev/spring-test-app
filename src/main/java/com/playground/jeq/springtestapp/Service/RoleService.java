package com.playground.jeq.springtestapp.Service;

import com.playground.jeq.springtestapp.Model.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    void saveRole(Role role);
}
