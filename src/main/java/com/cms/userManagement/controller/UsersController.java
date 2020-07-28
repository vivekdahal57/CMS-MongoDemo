package com.cms.userManagement.controller;

import com.cms.userManagement.entities.NewUserRequest;
import com.cms.userManagement.entities.Users;
import com.cms.userManagement.service.UsersService;
import com.cms.util.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * Created by i82325 on 7/2/2020.
 */
@CrossOrigin
@RestController
@RequestMapping(path = "/users")
public class UsersController {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UsersController.class);

    @Autowired
    UsersService usersService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Users> getAllUsers() {
        return usersService.getUsers();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addUser(@RequestBody NewUserRequest user) {
        return usersService.addNewUser(user);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody NewUserRequest user) {
        return usersService.updateUser(user);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public ResponseEntity<Users> updatePassword(@RequestBody Map<String, String> values) {
        return usersService.updatePassword(values.get("userName"), values.get("oldPassword"), values.get("newPassword"));
    }

    @RequestMapping(value = "/{_id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable BigInteger _id) {
        return usersService.getUserById(_id);
    }

    @DeleteMapping("/{_id}")
    public ResponseEntity<String> deleteUser(@PathVariable BigInteger _id) {
        return usersService.deleteUser(_id);
    }
}
