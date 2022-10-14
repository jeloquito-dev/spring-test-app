package com.playground.jeq.springtestapp;

import com.playground.jeq.springtestapp.Model.AppUser;
import com.playground.jeq.springtestapp.Model.Role;
import com.playground.jeq.springtestapp.Service.AppUserService;
import com.playground.jeq.springtestapp.Service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringTestAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTestAppApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner runner(AppUserService appUserService, RoleService roleService) {
		return args -> {
			roleService.saveRole(new Role(null, "ROLE_ADMIN"));
			roleService.saveRole(new Role(null, "ROLE_USER"));

			appUserService.saveAppUser(new AppUser(null, "Dahyun", "dahyun01", "password", new ArrayList<>()));
			appUserService.saveAppUser(new AppUser(null, "Sana", "sana01", "password", new ArrayList<>()));

			appUserService.addRoleToAppUser("dahyun01", "ROLE_ADMIN");
			appUserService.addRoleToAppUser("dahyun01", "ROLE_USER");
			appUserService.addRoleToAppUser("sana01", "ROLE_USER");
		};
	};
}
