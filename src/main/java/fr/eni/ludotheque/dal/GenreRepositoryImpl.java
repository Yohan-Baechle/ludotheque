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

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;
import fr.eni.ludotheque.exceptions.GenreNotFoundException;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    private static final Logger logger = LoggerFactory.getLogger(GenreRepositoryImpl.class);

    private static final String SELECT_ALL_GENRES = "SELECT id, libelle FROM genres";
    private static final String SELECT_GENRE_BY_ID = "SELECT id, libelle FROM genres WHERE id = :id";
    private static final String INSERT_GENRE = "INSERT INTO genres (libelle) VALUES (:libelle)";
    private static final String UPDATE_GENRE = "UPDATE genres SET libelle = :libelle WHERE id = :id";
    private static final String DELETE_GENRE = "DELETE FROM genres WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GenreRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(Genre genre) {
        try {
            namedParameterJdbcTemplate.update(INSERT_GENRE, new BeanPropertySqlParameterSource(genre));
            logger.info("Genre ajouté avec succès : {}", genre);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du genre : {}", genre, e);
            throw new DatabaseUpdateException("Échec de l'ajout du genre.", e);
        }
    }

    @Override
    public List<Genre> findAll() {
        try {
            return namedParameterJdbcTemplate.query(SELECT_ALL_GENRES, new BeanPropertyRowMapper<>(Genre.class));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des genres", e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des genres.", e);
        }
    }

    @Override
    public Optional<Genre> findById(int id) {
        try {
            Genre genre = namedParameterJdbcTemplate.queryForObject(SELECT_GENRE_BY_ID, Map.of("id", id),
                    new BeanPropertyRowMapper<>(Genre.class));
            return Optional.ofNullable(genre);
        } catch (Exception e) {
            logger.warn("Genre non trouvé avec l'ID : {}", id);
            return Optional.empty();
        }
    }

    @Override
    public void update(Genre genre) {
        try {
            int rows = namedParameterJdbcTemplate.update(UPDATE_GENRE, new BeanPropertySqlParameterSource(genre));
            if (rows != 1) {
                logger.error("Mise à jour échouée pour le genre : {}", genre);
                throw new DatabaseUpdateException("Mise à jour échouée pour le genre : " + genre);
            }
            logger.info("Genre mis à jour avec succès : {}", genre);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du genre : {}", genre, e);
            throw new DatabaseUpdateException("Erreur lors de la mise à jour du genre.", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int rows = namedParameterJdbcTemplate.update(DELETE_GENRE, Map.of("id", id));
            if (rows != 1) {
                logger.error("Suppression échouée pour le genre avec l'id : {}", id);
                throw new GenreNotFoundException("Suppression échouée pour le genre avec l'id : " + id);
            }
            logger.info("Genre supprimé avec succès, ID : {}", id);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du genre avec l'id : {}", id, e);
            throw new DatabaseUpdateException("Erreur lors de la suppression du genre.", e);
        }
    }
}
