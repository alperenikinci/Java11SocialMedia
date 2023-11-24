package com.bilgeadam.service;

import com.bilgeadam.manager.AuthManager;
import com.bilgeadam.mapper.UserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.UserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService extends ServiceManager<UserProfile,String> {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
    }


}
