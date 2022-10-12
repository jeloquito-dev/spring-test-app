package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Model.AppUser;
import com.playground.jeq.springtestapp.Service.AppUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/all")
    public List<AppUser> getAllAppUser() {
        return this.appUserService.getAllUser();
    }

}
