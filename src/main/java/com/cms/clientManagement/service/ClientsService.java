package com.cms.clientManagement.service;

import com.cms.clientManagement.entities.Clients;
import com.cms.clientManagement.repository.ClientsRepository;
import com.cms.userManagement.entities.Users;
import com.cms.userManagement.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by i82325 on 7/3/2020.
 */
@Service
public class ClientsService {
    @Autowired
    ClientsRepository clientsRepository;
    @Autowired
    UsersRepository usersRepository;

    @Transactional
    public List<Clients> getClients() {
        return clientsRepository.findAll();
    }

    @Transactional
    public Clients addNewClient(Clients client) {
        return clientsRepository.save(client);
    }

    @Transactional
    public Clients getClientByName(String clientName) {
        return clientsRepository.findByClientName(clientName);
    }
}
