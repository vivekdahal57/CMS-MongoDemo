package com.cms.clientManagement.controller;

import com.cms.clientManagement.entities.Clients;
import com.cms.clientManagement.service.ClientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by i82325 on 7/3/2020.
 */
@CrossOrigin
@RestController
@RequestMapping(path = "/clients")
public class ClientsController {
    @Autowired
    ClientsService clientsService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Clients> getAllClients() {
        return clientsService.getClients();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Clients addUser(@RequestBody Clients client) {
        return clientsService.addNewClient(client);
    }
    @RequestMapping(value = "/{clientName}", method = RequestMethod.GET)
    public Clients addUser(@PathVariable String clientName) {
        return clientsService.getClientByName(clientName);
    }

}
