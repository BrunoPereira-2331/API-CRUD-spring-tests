package com.bruno.api.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bruno.domain.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

}
