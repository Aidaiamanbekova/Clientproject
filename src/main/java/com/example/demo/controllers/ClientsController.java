package com.example.demo.controllers;

import java.util.Date;

import com.example.demo.entities.Client;
import com.example.demo.entities.ClientDto;
import com.example.demo.Repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.Valid;




@Controller
@RequestMapping("/clients")
public class ClientsController {
    @Autowired
    private ClientRepository clientRepo;
    
    @GetMapping({"","/"})
    public String getClients(Model model){
        var clients = clientRepo.findAll(Sort.by(Sort.Direction.DESC,"id"));
        model.addAttribute("clients", clients);

        return "clients/index";
    }


    @GetMapping("/create")
    public String createClient (Model model){
        ClientDto clientDto = new ClientDto();
        model.addAttribute("clientDto", clientDto);

        return "clients/create";
    }

    @PostMapping("/create")
    public String createClient(
    @Valid @ModelAttribute ClientDto clientDto,
    BindingResult result) {

        if (clientRepo.findByEmail(clientDto.getEmail()) !=null){
            result.addError(new FieldError("clientDto", "email", clientDto.getEmail()
            , false, null, null, "Email address is already used"));
        }

        if (result.hasErrors()){
            return "clients/create";
        }

        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());
        client.setCreatedAt(new Date());

        clientRepo.save(client);

        return "redirect:/clients";
    }


    @GetMapping("/edit")
    public String editClient(Model model, @RequestParam int id){
        Client client = clientRepo.findById(id).orElse(null);
        if (client == null){
            return "redirected/clients";
        }

        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName(client.getFirstName());
        clientDto.setLastName(client.getLastName());
        clientDto.setEmail(client.getEmail());
        clientDto.setPhone(client.getPhone());
        clientDto.setAddress(client.getAddress());
        clientDto.setStatus(client.getStatus());

        model.addAttribute("client", client);
        model.addAttribute("clientDto",clientDto);
        
        return "clients/edit";

    }

    @PostMapping("/edit")
    public String editClient(
        Model model,
        @RequestParam int id, @ModelAttribute ClientDto clientDto,
        BindingResult result){

            Client client = clientRepo.findById(id).orElse(null);
            if (client == null){
                return "redirected/clients";
            }

            model.addAttribute("client", client);
            if (result.hasErrors()){
                return "clients/edit";
            }


            //update client deatails
            client.setFirstName(clientDto.getFirstName());
            client.setLastName(clientDto.getLastName());
            client.setEmail(clientDto.getEmail());
            client.setPhone(clientDto.getPhone());
            client.setAddress(clientDto.getAddress());
            client.setStatus(clientDto.getStatus());

            try {
                //for unique email
                clientRepo.save(client);
            } catch (Exception ex){
                result.addError(
                    new FieldError("clientDto", "email", clientDto.getEmail(),
                    false, null, null, "Email address is already used")
                );
                return "clients/edit";

            }
    
            return "redirected/clients";
    }

    @SuppressWarnings("null")
    @GetMapping("/delete")
    public String deleteClient(@RequestParam int id){
        Client client = clientRepo.findById(id).orElse(null);
        if(client == null){
            clientRepo.delete(client);
        }
        return "redirect/clients";
    }


}

