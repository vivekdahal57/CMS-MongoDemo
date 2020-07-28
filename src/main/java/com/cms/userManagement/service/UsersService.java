package com.cms.userManagement.service;

import com.cms.clientManagement.entities.Clients;
import com.cms.clientManagement.repository.ClientsRepository;
import com.cms.userManagement.entities.NewUserRequest;
import com.cms.userManagement.entities.Roles;
import com.cms.userManagement.entities.Users;
import com.cms.userManagement.repository.RolesRepository;
import com.cms.userManagement.repository.UsersRepository;
import com.cms.util.MessageResponse;
import com.cms.util.PasswordResolveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by i82325 on 7/2/2020.
 */
@Service
public class UsersService implements UserDetailsService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    ClientsRepository clientsRepository;
    @Autowired
    PasswordResolveUtil passwordResolveUtil;

    @Transactional
    public List<Users> getUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().toArray()[0].toString().equals("SUPER_ADMIN")) {
            return usersRepository.findAll();
        } else {
            String currentUserName = auth.getName().split("\\$")[0];
            Users currentUser = usersRepository.findByUserName(currentUserName);
            return usersRepository.findByClients(currentUser.getClients());
        }
    }

    @Transactional
    public ResponseEntity<?> addNewUser(NewUserRequest user) {
        if (usersRepository.existsByUserName(user.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        if (usersRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        Users u = new Users();
        u.setUserName(user.getUserName());
        u.setPassword(passwordResolveUtil.getResolvedPassword(user.getUserName(), user.getPassword()));
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        u.setRoles(getRoleFromUser(user));
        u.setClients(getClientFromUser(user));

        usersRepository.save(u);
        return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.CREATED);

    }

    @Transactional
    public ResponseEntity<Users> updateUser(NewUserRequest user) {
        if (usersRepository.existsByUserName(user.getUserName())) {
            Users u = usersRepository.findByUserName(user.getUserName());
            u.setLastName(user.getLastName());
            u.setFirstName(user.getFirstName());
            u.setEmail(user.getEmail());
            u.setRoles(getRoleFromUser(user));
            u.setClients(getClientFromUser(user));
            usersRepository.save(u);
            return new ResponseEntity<>(u, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<Users> getUserById(BigInteger id) {
        try {
            return new ResponseEntity<>(usersRepository.findBy_id(id), new HttpHeaders(), HttpStatus.OK);
        } catch (Exception e) {

        }
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<Users> updatePassword(String userName, String oldPassword, String newPassword) {
        try {
            Users u = usersRepository.findByUserName(userName);
            if (u != null) {
                String oldPassResolved = passwordResolveUtil.getResolvedPassword(u.getUserName(), oldPassword);
                String newPassResolved = passwordResolveUtil.getResolvedPassword(u.getUserName(), newPassword);
                if (!u.getPassword().equalsIgnoreCase(newPassResolved) && u.getPassword().equalsIgnoreCase(oldPassResolved)) {
                    u.setPassword(newPassResolved);
                    usersRepository.save(u);
                    return new ResponseEntity<>(u, new HttpHeaders(), HttpStatus.OK);
                }
            }
        } catch (Exception e) {

        }
        return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<String> deleteUser(BigInteger _id) {
        Users user = usersRepository.findBy_id(_id);
        if (user != null && !user.getRoles().getRoleName().equals(rolesRepository.findByRoleName("SUPER_ADMIN").get_id())) {
            usersRepository.delete(usersRepository.findBy_id(_id));
            return new ResponseEntity<>(
                    "User with id " + _id + " is deleted!!", new HttpHeaders(), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(
                "User Cannot be Deleted!! Either it does not exists or its a Super admin!!", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        try {
            Users newUser = usersRepository.findByUserName(userName);

            if (newUser == null) {
                throw new UsernameNotFoundException("No user found with username: " + userName);
            }
            org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(newUser.getUserName() + "$" + newUser.getFirstName() + " " + newUser.getLastName(), newUser.getPassword(), true, true, true, true, getAuthorities("" + newUser.getRoles().getRoleName()));
            return userDetails;
        } catch (Exception e) {
            new RuntimeException(e);
            return null;
        }
    }

    private final Collection<? extends GrantedAuthority> getAuthorities(String roleId) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleId));
        return authorities;
    }

    private Roles getRoleFromUser(NewUserRequest user) {
        Roles roles;
        String requestRole = user.getRole();
        if (requestRole == null) {
            roles = rolesRepository.findByRoleName("PUBLIC");
        } else if (rolesRepository.existsByRoleName(requestRole)) {
            roles = rolesRepository.findByRoleName(requestRole);
        } else {
            roles = rolesRepository.findByRoleName("PUBLIC");
        }
        return roles;
    }

    private Clients getClientFromUser(NewUserRequest user) {
        Clients clients;
        if (clientsRepository.existsByClientName(user.getClient())) {
            clients = clientsRepository.findByClientName(user.getClient());
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = auth.getName().split("\\$")[0];
            Users currentUser = usersRepository.findByUserName(currentUserName);
            if (!auth.getAuthorities().toArray()[0].toString().equals("SUPER_ADMIN")) {
                clients = clientsRepository.findByClientName(currentUser.getClients().getClientName());
            } else {
                clients = null;
            }
        }
        return clients;
    }
}