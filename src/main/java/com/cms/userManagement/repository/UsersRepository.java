package com.cms.userManagement.repository;

import com.cms.clientManagement.entities.Clients;
import com.cms.userManagement.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by i82325 on 7/2/2020.
 */
@Repository
public interface UsersRepository extends MongoRepository<Users, String> {
    Users findByUserName(String userName);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Users findBy_id(BigInteger _id);

    List<Users> findByClients(Clients clients);
}
