package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.UserProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends ElasticsearchRepository<UserProfile,String> {
    Optional<UserProfile> findOptionalByAuthId(Long authId);
    Optional<UserProfile> findOptionalByUsernameIgnoreCase(String username);
}
