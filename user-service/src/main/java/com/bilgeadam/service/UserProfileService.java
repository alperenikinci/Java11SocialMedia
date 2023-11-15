package com.bilgeadam.service;

import com.bilgeadam.dto.request.ActivateStatusRequestDto;
import com.bilgeadam.dto.request.UserCreateRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.mapper.UserMapper;
import com.bilgeadam.repository.UserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.ServiceManager;
import com.bilgeadam.utility.enums.EStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile,Long> {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
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
}
