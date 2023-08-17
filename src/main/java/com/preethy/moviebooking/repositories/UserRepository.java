package com.preethy.moviebooking.repositories;

import com.preethy.moviebooking.collections.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserInfo, String> {

    Optional<UserInfo> findByUsername(String username);

}
