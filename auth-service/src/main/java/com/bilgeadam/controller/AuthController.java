package com.bilgeadam.controller;

import com.bilgeadam.dto.request.ActivationRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    /*
       Register olduktan sonra bize bir activationCode geliyor.
       Bu activation Code'u kullanarak user'ımın statusunu degistirmek istiyorum.
       Buna uygun bir metot yazalim.
     */

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto>  register (@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/activate_status")
    public ResponseEntity<Boolean> activateStatus(ActivationRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }


}
