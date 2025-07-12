package com.api.server.service.client;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.dto.client.SaveClientDTO;
import com.api.server.dto.client.ShowClientDTO;
import com.api.server.persistence.entity.client.Client;
    
public interface ClientService {

    Page<ShowClientDTO> findAll(String clienttype, String status, Pageable pageable);

    ShowClientDTO findClientById(Long id);

    Optional<ShowClientDTO> findClientByDocumentnumber(String documentnumber);

    ShowClientDTO saveClient(SaveClientDTO clientDTO);

    ShowClientDTO findClientByClienttypeAndStatus(Client.ClientType clienttype, Client.ClientStatus status);
    
    ShowClientDTO disableClient(Long id);

    ShowClientDTO updateClient(Long id, SaveClientDTO clientDTO);
}
