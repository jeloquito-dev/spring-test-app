package com.playground.jeq.springtestapp.Controller;

import com.playground.jeq.springtestapp.Model.API.BaseResponse;
import com.playground.jeq.springtestapp.Model.AppUser;
import com.playground.jeq.springtestapp.Service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.playground.jeq.springtestapp.Config.Utility.StringReference.USER_ALL;
import static com.playground.jeq.springtestapp.Config.Utility.StringReference.USER_BASE;

@RestController
@RequestMapping(USER_BASE)
public class UserController {

    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping(USER_ALL)
    public ResponseEntity<BaseResponse<List<AppUser>>> getAllAppUser() {
        return new ResponseEntity<>(
                new BaseResponse<>("", 200, appUserService.getAllUser()),
                HttpStatus.OK
        );
    }

}
