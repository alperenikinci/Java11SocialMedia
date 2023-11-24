package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequestDto {

    private String token;
    private String username;
    private String email;
    private String phone;
    private String avatarUrl;
    private String address;
    private String about;
}
