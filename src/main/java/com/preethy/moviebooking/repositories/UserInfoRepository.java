package com.preethy.moviebooking.repositories;

import com.preethy.moviebooking.collections.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo, String> {

    UserInfo findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
