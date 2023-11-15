package com.bilgeadam.manager;

import com.bilgeadam.dto.request.ActivateStatusRequestDto;
import com.bilgeadam.dto.request.UserCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.bilgeadam.constants.RestApi.ACTIVATESTATUS;


@FeignClient(url = "http://localhost:7071/api/v1/user", name = "auth-userprofile")
public interface UserManager {

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestBody UserCreateRequestDto dto);

    @GetMapping(ACTIVATESTATUS+"/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);

    @PostMapping(ACTIVATESTATUS+"2")
    public ResponseEntity<Boolean> activateStatus2(@RequestBody ActivateStatusRequestDto dto);

}
