package com.api.server.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.api.server.dto.client.SaveClientDTO;
import com.api.server.dto.client.ShowClientDTO;
import com.api.server.dto.security.SaveUser;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.client.Client;
import com.api.server.persistence.entity.client.ClientNatural;
import com.api.server.persistence.entity.client.ClientJuridico;
import com.api.server.persistence.repository.client.ClientRepository;
import com.api.server.service.client.ClientService;
import com.api.server.service.security.UserService;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserService userService;

    @Override
    public Page<ShowClientDTO> findAll(String clienttype, String status, Pageable pageable) {
        Page<Client> clients;
        if (clienttype != null && status != null) {
            clients = clientRepository.findByClienttypeAndStatus(
                Client.ClientType.valueOf(clienttype), 
                Client.ClientStatus.valueOf(status), 
                pageable
            );
        } else if (clienttype != null) {
            clients = clientRepository.findByClienttype(
                Client.ClientType.valueOf(clienttype), 
                pageable
            );
        } else if (status != null) {
            clients = clientRepository.findByStatus(
                Client.ClientStatus.valueOf(status), 
                pageable
            );
        } else {
            clients = clientRepository.findAll(pageable);
        }
        
        return clients.map(this::mapToDTO);
    }

    @Override
    public ShowClientDTO findClientById(Long id) {
        return clientRepository.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente no encontrado"));
    }    

    @Override
    public Optional<ShowClientDTO> findClientByDocumentnumber(String documentnumber) {
        return clientRepository.findByDocumentnumber(documentnumber)
            .stream()
            .findFirst()
            .map(this::mapToDTO);
    }

    @Override
    public ShowClientDTO saveClient(SaveClientDTO clientDTO) {
        Client client;
        
        if (clientDTO.getClientType() == Client.ClientType.NATURAL) {
            ClientNatural naturalClient = new ClientNatural();
            naturalClient.setName(clientDTO.getName());
            naturalClient.setLastname(clientDTO.getLastname());
            naturalClient.setBirthdate(clientDTO.getBirthdate());
            client = naturalClient;
        } else {
            ClientJuridico juridicClient = new ClientJuridico();
            juridicClient.setRazonsocial(clientDTO.getRazonsocial());
            client = juridicClient;
        }
        
        // Set common fields
        client.setEmail(clientDTO.getEmail());
        client.setContact(clientDTO.getContact());
        client.setAddress(clientDTO.getAddress());
        client.setClienttype(clientDTO.getClientType());
        client.setDocumentnumber(clientDTO.getDocumentnumber());
        client.setStatus(clientDTO.getStatus() != null ? clientDTO.getStatus() : Client.ClientStatus.ENABLED);
        
        // Guardar el cliente
        Client savedClient = clientRepository.save(client);

        // Crear usuario asociado al cliente
        SaveUser newUser = new SaveUser();
        newUser.setUsername(clientDTO.getDocumentnumber());
        newUser.setPassword(clientDTO.getDocumentnumber());
        newUser.setRepeatedPassword(clientDTO.getDocumentnumber());
        newUser.setName(clientDTO.getClientType() == Client.ClientType.NATURAL ? clientDTO.getName() : clientDTO.getRazonsocial());
        newUser.setLastname(clientDTO.getClientType() == Client.ClientType.NATURAL ? clientDTO.getLastname() : "");
        newUser.setEmail(clientDTO.getEmail());
        newUser.setContacto(clientDTO.getContact());
        newUser.setRole("Cliente");
        newUser.setStatus("ENABLED");

        userService.registerOneUser(newUser);
        
        return mapToDTO(savedClient);
    }

    @Override
    public ShowClientDTO findClientByClienttypeAndStatus(Client.ClientType clienttype, Client.ClientStatus status) {
        return clientRepository.findByClienttypeAndStatus(clienttype, status, Pageable.unpaged())
            .stream()
            .findFirst()
            .map(this::mapToDTO)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente no encontrado"));
    }

    @Override
    public ShowClientDTO disableClient(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente no encontrado"));
        client.setStatus(Client.ClientStatus.DISABLED);
        return mapToDTO(clientRepository.save(client));
    }

    @Override
    public ShowClientDTO updateClient(Long id, SaveClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente no encontrado"));

        // Validar que el tipo de cliente coincida
        if (clientDTO.getClientType() != null && clientDTO.getClientType() != client.getClienttype()) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de cliente");
        }

        // Actualizar campos comunes
        if (StringUtils.hasText(clientDTO.getEmail())) {
            client.setEmail(clientDTO.getEmail());
        }
        if (StringUtils.hasText(clientDTO.getContact())) {
            client.setContact(clientDTO.getContact());
        }
        if (StringUtils.hasText(clientDTO.getAddress())) {
            client.setAddress(clientDTO.getAddress());
        }
        if (StringUtils.hasText(clientDTO.getDocumentnumber())) {
            client.setDocumentnumber(clientDTO.getDocumentnumber());
        }

        // Actualizar el estado del cliente
        if (clientDTO.getStatus() != null) {
            client.setStatus(clientDTO.getStatus());
        }

        // Actualizar campos específicos según el tipo
        if (client instanceof ClientNatural && clientDTO.getClientType() == Client.ClientType.NATURAL) {
            ClientNatural naturalClient = (ClientNatural) client;
            if (StringUtils.hasText(clientDTO.getName())) {
                naturalClient.setName(clientDTO.getName());
            }
            if (StringUtils.hasText(clientDTO.getLastname())) {
                naturalClient.setLastname(clientDTO.getLastname());
            }
            if (clientDTO.getBirthdate() != null) {
                naturalClient.setBirthdate(clientDTO.getBirthdate());
            }
        } else if (client instanceof ClientJuridico && clientDTO.getClientType() == Client.ClientType.JURIDICO) {
            ClientJuridico juridicClient = (ClientJuridico) client;
            if (StringUtils.hasText(clientDTO.getRazonsocial())) {
                juridicClient.setRazonsocial(clientDTO.getRazonsocial());
            }
        }

        return mapToDTO(clientRepository.save(client));
    }

    private ShowClientDTO mapToDTO(Client client) {
        ShowClientDTO dto = new ShowClientDTO();
        dto.setId(client.getId());
        dto.setEmail(client.getEmail());
        dto.setContact(client.getContact());
        dto.setAddress(client.getAddress());
        dto.setClientType(client.getClienttype());
        dto.setStatus(client.getStatus());
        dto.setDocumentnumber(client.getDocumentnumber());
        
        if (client instanceof ClientNatural) {
            ClientNatural natural = (ClientNatural) client;
            dto.setName(natural.getName());
            dto.setLastname(natural.getLastname());
            dto.setBirthdate(natural.getBirthdate());
        } else if (client instanceof ClientJuridico) {
            ClientJuridico juridic = (ClientJuridico) client;
            dto.setRazonsocial(juridic.getRazonsocial());
        }
        
        return dto;
    }
} 