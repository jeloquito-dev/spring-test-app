package com.playground.jeq.springtestapp.Service.Impl;

import com.playground.jeq.springtestapp.Model.AppUser;
import com.playground.jeq.springtestapp.Model.Role;
import com.playground.jeq.springtestapp.Repository.AppUserRepository;
import com.playground.jeq.springtestapp.Repository.RoleRepository;
import com.playground.jeq.springtestapp.Service.AppUserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private AppUserRepository appUserRepository;
    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveAppUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
    }

    @Override
    @Transactional
    public void addRoleToAppUser(String username, String roleDescription) {
        Role role = roleRepository.findByDescription(roleDescription);
        AppUser appUser = appUserRepository.findByUsername(username);
        appUser.getRoles().add(role);
    }

    @Override
    public AppUser getAppUserById(Long id) {
        return appUserRepository.getOne(id);
    }

    @Override
    public AppUser findAppUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getAllUser() {
        return appUserRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (null == appUser) {
            throw new UsernameNotFoundException("User not found in the database.");
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            appUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getDescription())));
            return new User(appUser.getUsername(), appUser.getPassword(), authorities);
        }

    }
}
