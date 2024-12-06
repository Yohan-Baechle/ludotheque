package fr.eni.ludotheque.dal;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.ludotheque.bo.Genre;

@Repository
@Primary
public class GenreRepositoryImpl implements GenreRepository {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public GenreRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public void add(Genre genre) {
		String sql = "INSERT INTO genres (libelle) VALUES (:libelle)";
		int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(genre));
		if (rows != 1) {
			throw new RuntimeException("Insertion échouée pour le genre : " + genre);
		}
	}

	@Override
	public List<Genre> findAll() {
		String sql = "SELECT id, libelle FROM genres";
		return namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class));
	}

	@Override
	public Optional<Genre> findById(int id) {
		String sql = "SELECT id, libelle FROM genres WHERE id = :id";
		try {
			Genre genre = namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id),
					new BeanPropertyRowMapper<>(Genre.class));
			return Optional.ofNullable(genre);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public void update(Genre genre) {
		String sql = "UPDATE genres SET libelle = :libelle WHERE id = :id";
		int rows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(genre));
		if (rows != 1) {
			throw new RuntimeException("Mise à jour échouée pour le genre : " + genre);
		}
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM genres WHERE id = :id";
		int rows = namedParameterJdbcTemplate.update(sql, Map.of("id", id));
		if (rows != 1) {
			throw new RuntimeException("Suppression échouée pour le genre avec l'id : " + id);
		}
	}
}
