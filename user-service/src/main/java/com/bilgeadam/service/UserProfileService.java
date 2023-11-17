package com.bilgeadam.service;

import com.bilgeadam.dto.request.ActivateStatusRequestDto;
import com.bilgeadam.dto.request.UserCreateRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.mapper.UserMapper;
import com.bilgeadam.repository.UserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import com.bilgeadam.utility.enums.EStatus;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile,Long> {

    private final UserProfileRepository userProfileRepository;
    private final JwtTokenManager jwtTokenManager;

    public UserProfileService(UserProfileRepository userProfileRepository,JwtTokenManager jwtTokenManager) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public Boolean createUser(UserCreateRequestDto dto) {
        try {
            save(UserMapper.INSTANCE.fromCreateRequestToUser(dto));
            return true;
        } catch (Exception e){
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    public Boolean activateStatus(Long authId) {
      Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId);
      if(userProfile.isEmpty()){
          throw new UserManagerException(ErrorType.USER_NOT_FOUND);
      }else {
          userProfile.get().setStatus(EStatus.ACTIVE);
          update(userProfile.get());
          return true;
      }
    }

    public Boolean activateStatus2(ActivateStatusRequestDto dto) {
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(dto.getAuthId());
        if(userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }else {
            userProfile.get().setStatus(EStatus.ACTIVE);
            update(userProfile.get());
            return true;
        }
    }

    public Boolean update(UserProfileUpdateRequestDto dto){
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if(authId.isEmpty()){
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = userProfileRepository.findOptionalByAuthId(authId.get());
        if(userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setUsername(dto.getUsername());
        userProfile.get().setEmail(dto.getEmail());
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAvatarUrl(dto.getAvatarUrl());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setAbout(dto.getAbout());
        update(userProfile.get());
        return true;
    }
}
