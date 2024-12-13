package com.example.demo.Repositories;

import com.example.demo.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository <Client, Integer > {

    public Client findByEmail (String email);

    public void save(ch.qos.logback.core.net.server.Client client);


     
}
