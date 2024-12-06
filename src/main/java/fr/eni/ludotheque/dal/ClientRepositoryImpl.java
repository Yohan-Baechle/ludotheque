package fr.eni.ludotheque.dal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.ludotheque.bo.Client;

@Repository
@Primary
public class ClientRepositoryImpl implements ClientRepository {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public ClientRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public void add(Client client) {
		String sql = "INSERT INTO clients (nom, prenom, email, no_telephone, rue, code_postal, ville) "
				+ "VALUES (:nom, :prenom, :email, :noTelephone, :rue, :codePostal, :ville)";
		namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(client));
	}

	@Override
	public List<Client> findAll() {
		String sql = "SELECT id, nom, prenom, email, no_telephone AS noTelephone, rue, code_postal AS codePostal, ville "
				+ "FROM clients";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Client.class));
	}

	@Override
	public Optional<Client> findById(int id) {
		String sql = "SELECT id, nom, prenom, email, no_telephone AS noTelephone, rue, code_postal AS codePostal, ville "
				+ "FROM clients WHERE id = :id";
		try {
			Client client = namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id),
					new BeanPropertyRowMapper<>(Client.class));
			return Optional.ofNullable(client);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public void update(Client client) {
		String sql = "UPDATE clients SET nom = :nom, prenom = :prenom, email = :email, no_telephone = :noTelephone, "
				+ "rue = :rue, code_postal = :codePostal, ville = :ville WHERE id = :id";
		int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(client));
		if (rows != 1) {
			throw new RuntimeException("Mise à jour échouée pour le client : " + client);
		}
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM clients WHERE id = :id";
		int rows = namedParameterJdbcTemplate.update(sql, Map.of("id", id));
		if (rows != 1) {
			throw new RuntimeException("Suppression échouée pour le client avec l'id : " + id);
		}
	}
}
