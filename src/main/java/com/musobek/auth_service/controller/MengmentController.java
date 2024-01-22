package com.musobek.auth_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mengment")
@PreAuthorize("hasAnyRole('ADMIN','MENGER')") // bu shu yerdagi barcha betedlar uchun ishlaydi
public class MengmentController {

    @PostMapping
    public String post(){
        return "Post: Mengment post";
    }


}
