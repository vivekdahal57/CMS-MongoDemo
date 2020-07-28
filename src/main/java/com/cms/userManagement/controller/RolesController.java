package com.cms.userManagement.controller;

import com.cms.userManagement.entities.Roles;
import com.cms.userManagement.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by i82325 on 7/3/2020.
 */
@CrossOrigin
@RestController
@RequestMapping(path = "/roles")
public class RolesController {
    @Autowired
    RolesService rolesService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Roles> getAllUsers() {
        return rolesService.getRoles();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity addRoles(@RequestBody Roles roles) {
        return rolesService.addNewRoles(roles);
    }

    @RequestMapping(value = "/{roleName}", method = RequestMethod.GET)
    public ResponseEntity getRoles(@PathVariable String roleName) {
        return rolesService.getRoleByRoleName(roleName);
    }
}
