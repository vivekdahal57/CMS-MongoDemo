package com.cms.userManagement.repository;

import com.cms.userManagement.entities.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by i82325 on 7/2/2020.
 */
@Repository
public interface RolesRepository extends MongoRepository<Roles, String> {
    boolean existsByRoleName(String roleName);

    Roles findByRoleName(String roleName);
}
