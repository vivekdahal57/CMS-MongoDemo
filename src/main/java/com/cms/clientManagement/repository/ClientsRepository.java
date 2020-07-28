package com.cms.clientManagement.repository;

import com.cms.clientManagement.entities.Clients;
import com.cms.userManagement.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by i82325 on 7/2/2020.
 */
@Repository
public interface ClientsRepository extends MongoRepository<Clients, String> {
    Clients findByClientName(String clientName);

    boolean existsByClientName(String clientName);
}
