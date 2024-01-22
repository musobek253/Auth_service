package com.musobek.auth_service.controller;

import com.musobek.auth_service.entity.User;
import com.musobek.auth_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;


    @PostMapping
    public String post(){
        return "post: get metod admin ";
    }
    @GetMapping
    public List<User> allUsers(){
        return  authenticationService.getallusers();
    }
    @PutMapping
    public String update(){
        return "update: get metod";
    }
    @DeleteMapping
    public String delete(){
        return "delete: get metod";
    }


}
