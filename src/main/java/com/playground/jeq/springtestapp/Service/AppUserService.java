package com.playground.jeq.springtestapp.Service;

import com.playground.jeq.springtestapp.Model.Entity.AppUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppUserService {

    void saveAppUser(AppUser appUser);

    void addRoleToAppUser(String username, String roleName);

    AppUser getAppUserById(Long id);
    AppUser findAppUserByUsername(String username);
    List<AppUser> getAllUser();

}
