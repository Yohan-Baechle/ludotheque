package fr.eni.ludotheque.bll;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.eni.ludotheque.bo.Client;
import fr.eni.ludotheque.dal.ClientRepository;
import fr.eni.ludotheque.exceptions.ClientNotFoundException;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void create(Client client) {
        try {
            clientRepository.add(client);
            logger.info("Client créé : {}", client);
        } catch (Exception e) {
            logger.error("Erreur lors de la création du client : {}", client, e);
            throw new DatabaseUpdateException("Échec de la création du client.", e);
        }
    }

    @Override
    public List<Client> findAll() {
        try {
            return clientRepository.findAll();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des clients", e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des clients.", e);
        }
    }

    @Override
    public Optional<Client> findById(int id) {
        return clientRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        Optional<Client> clientOpt = findById(id);
        if (clientOpt.isPresent()) {
            try {
                clientRepository.delete(id);
                logger.info("Client supprimé, ID : {}", id);
            } catch (Exception e) {
                logger.error("Erreur lors de la suppression du client avec l'id : {}", id, e);
                throw new DatabaseUpdateException("Erreur lors de la suppression du client.", e);
            }
        } else {
            logger.error("Client non trouvé lors de la suppression, ID : {}", id);
            throw new ClientNotFoundException("Client introuvable avec l'ID : " + id);
        }
    }

    @Override
    public void saveOrUpdate(Client client) {
        if (client.getId() == null) {
            this.create(client);  // Création si l'ID est null
        } else {
            Optional<Client> clientOpt = findById(client.getId());
            if (clientOpt.isPresent()) {
                try {
                    clientRepository.update(client);
                    logger.info("Client mis à jour : {}", client);
                } catch (Exception e) {
                    logger.error("Erreur lors de la mise à jour du client : {}", client, e);
                    throw new DatabaseUpdateException("Erreur lors de la mise à jour du client.", e);
                }
            } else {
                logger.error("Client introuvable lors de la mise à jour, ID : {}", client.getId());
                throw new ClientNotFoundException("Client introuvable avec l'ID : " + client.getId());
            }
        }
    }
}
