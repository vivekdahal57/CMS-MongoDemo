package com.cms.userManagement.service;

import com.cms.userManagement.entities.Roles;
import com.cms.userManagement.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Created by i82325 on 7/2/2020.
 */
@Service
public class RolesService {
    @Autowired
    RolesRepository rolesRepository;

    @Transactional
    public List<Roles> getRoles() {
        return rolesRepository.findAll();
    }

    @Transactional
    public ResponseEntity addNewRoles(Roles roles) {
        if (!rolesRepository.existsByRoleName(roles.roleName)) {
            rolesRepository.save(roles);
            return ResponseEntity.ok().body(roles);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, roles.getRoleName() + " already exists!");
        }


    }

    @Transactional
    public ResponseEntity getRoleByRoleName(String roleName) {
        if (rolesRepository.existsByRoleName(roleName)) {
            Roles r = rolesRepository.findByRoleName(roleName);
            return ResponseEntity.ok().body(r);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, roleName + " already exists!");
        }
    }

}