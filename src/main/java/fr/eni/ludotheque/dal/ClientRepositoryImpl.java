package fr.eni.ludotheque.dal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.ludotheque.bo.Client;
import fr.eni.ludotheque.exceptions.ClientNotFoundException;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private static final Logger logger = LoggerFactory.getLogger(ClientRepositoryImpl.class);

    private static final String SELECT_ALL_CLIENTS = "SELECT id, nom, prenom, email, no_telephone AS noTelephone, rue, code_postal AS codePostal, ville FROM clients";
    private static final String SELECT_CLIENT_BY_ID = "SELECT id, nom, prenom, email, no_telephone AS noTelephone, rue, code_postal AS codePostal, ville FROM clients WHERE id = :id";
    private static final String INSERT_CLIENT = "INSERT INTO clients (nom, prenom, email, no_telephone, rue, code_postal, ville) VALUES (:nom, :prenom, :email, :noTelephone, :rue, :codePostal, :ville)";
    private static final String UPDATE_CLIENT = "UPDATE clients SET nom = :nom, prenom = :prenom, email = :email, no_telephone = :noTelephone, rue = :rue, code_postal = :codePostal, ville = :ville WHERE id = :id";
    private static final String DELETE_CLIENT = "DELETE FROM clients WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ClientRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(Client client) {
        try {
            namedParameterJdbcTemplate.update(INSERT_CLIENT, new BeanPropertySqlParameterSource(client));
            logger.info("Client ajouté avec succès : {}", client);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du client : {}", client, e);
            throw new DatabaseUpdateException("Échec de l'ajout du client.", e);
        }
    }

    @Override
    public List<Client> findAll() {
        try {
            return namedParameterJdbcTemplate.query(SELECT_ALL_CLIENTS, new BeanPropertyRowMapper<>(Client.class));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des clients", e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des clients.", e);
        }
    }

    @Override
    public Optional<Client> findById(int id) {
        try {
            Client client = namedParameterJdbcTemplate.queryForObject(SELECT_CLIENT_BY_ID, Map.of("id", id), new BeanPropertyRowMapper<>(Client.class));
            return Optional.ofNullable(client);
        } catch (Exception e) {
            logger.warn("Client non trouvé avec l'ID : {}", id);
            return Optional.empty();
        }
    }

    @Override
    public void update(Client client) {
        try {
            int rows = namedParameterJdbcTemplate.update(UPDATE_CLIENT, new BeanPropertySqlParameterSource(client));
            if (rows != 1) {
                logger.error("Mise à jour échouée pour le client : {}", client);
                throw new DatabaseUpdateException("Mise à jour échouée pour le client : " + client);
            }
            logger.info("Client mis à jour avec succès : {}", client);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du client : {}", client, e);
            throw new DatabaseUpdateException("Erreur lors de la mise à jour du client.", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int rows = namedParameterJdbcTemplate.update(DELETE_CLIENT, Map.of("id", id));
            if (rows != 1) {
                logger.error("Suppression échouée pour le client avec l'id : {}", id);
                throw new ClientNotFoundException("Suppression échouée pour le client avec l'id : " + id);
            }
            logger.info("Client supprimé avec succès, ID : {}", id);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du client avec l'id : {}", id, e);
            throw new DatabaseUpdateException("Erreur lors de la suppression du client.", e);
        }
    }
}
