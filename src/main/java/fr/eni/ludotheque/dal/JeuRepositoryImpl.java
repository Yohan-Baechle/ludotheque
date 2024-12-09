package fr.eni.ludotheque.dal;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.bo.Jeu;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;
import fr.eni.ludotheque.exceptions.JeuNotFoundException;

@Repository
public class JeuRepositoryImpl implements JeuRepository {
    private static final Logger logger = LoggerFactory.getLogger(JeuRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public JeuRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String INSERT_JEU = "INSERT INTO jeux (titre, reference, description, tarif_journee, age_min, duree) "
            + "VALUES (:titre, :reference, :description, :tarifJournee, :ageMin, :duree)";
    private static final String SELECT_ALL_JEUX = "SELECT id, titre, reference, description, tarif_journee, age_min, duree FROM jeux";
    private static final String SELECT_JEU_BY_ID = "SELECT id, titre, reference, description, tarif_journee, age_min, duree FROM jeux WHERE id = ?";
    private static final String DELETE_JEU = "DELETE FROM jeux WHERE id = ?";
    private static final String DELETE_JEU_GENRE = "DELETE FROM jeux_genres WHERE jeu_id = ?";
    private static final String INSERT_JEU_GENRE = "INSERT INTO jeux_genres (jeu_id, genre_id) VALUES (:jeuId, :genreId)";
    private static final String UPDATE_JEU = "UPDATE jeux SET titre = :titre, reference = :reference, description = :description, "
            + "tarif_journee = :tarifJournee, age_min = :ageMin, duree = :duree WHERE id = :id";
    private static final String SELECT_GENRES_BY_JEU_ID = "SELECT genres.id, genres.libelle "
            + "FROM jeux_genres INNER JOIN genres ON jeux_genres.genre_id = genres.id "
            + "WHERE jeux_genres.jeu_id = ?";

    // DTO utilisé pour l'association entre jeux et genres
    record JeuGenreDto(Integer jeuId, Integer genreId) {}

    @Override
    @Transactional
    public void add(Jeu newJeu) {
        try {
            // Insertion du jeu
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(INSERT_JEU, new BeanPropertySqlParameterSource(newJeu), keyHolder, new String[]{"id"});
            Integer jeuId = keyHolder.getKeyAs(Integer.class);
            logger.info("Jeu ajouté avec succès, ID : {}", jeuId);
            newJeu.setId(jeuId);

            // Insertion des genres associés
            List<JeuGenreDto> jeuGenreDtos = newJeu.getGenres().stream()
                    .map(genre -> new JeuGenreDto(jeuId, genre.getId()))
                    .toList();
            SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(jeuGenreDtos);
            namedParameterJdbcTemplate.batchUpdate(INSERT_JEU_GENRE, batchArgs);
            logger.info("Genres associés ajoutés au jeu, ID : {}", jeuId);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du jeu : {}", newJeu, e);
            throw new DatabaseUpdateException("Échec de l'ajout du jeu.", e);
        }
    }

    @Override
    public List<Jeu> findAll() {
        try {
            return namedParameterJdbcTemplate.query(SELECT_ALL_JEUX, new BeanPropertyRowMapper<>(Jeu.class));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des jeux", e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des jeux.", e);
        }
    }

    @Override
    public Optional<Jeu> findById(int id) {
        try {
            Jeu jeu = jdbcTemplate.queryForObject(SELECT_JEU_BY_ID, new BeanPropertyRowMapper<>(Jeu.class), id);
            List<Genre> genres = getGenresByJeuId(jeu.getId());
            jeu.setGenres(genres);
            return Optional.ofNullable(jeu);
        } catch (Exception e) {
            logger.warn("Jeu non trouvé, ID : {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public void update(Jeu jeu) {
        try {
            int nbRows = namedParameterJdbcTemplate.update(UPDATE_JEU, new BeanPropertySqlParameterSource(jeu));
            if (nbRows != 1) {
                throw new RuntimeException("La modification du jeu a échouée : " + jeu);
            }

            // Suppression des genres existants et ajout des nouveaux
            jdbcTemplate.update(DELETE_JEU_GENRE, jeu.getId());
            List<JeuGenreDto> jeuGenreDtos = jeu.getGenres().stream()
                    .map(genre -> new JeuGenreDto(jeu.getId(), genre.getId()))
                    .toList();
            SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(jeuGenreDtos);
            namedParameterJdbcTemplate.batchUpdate(INSERT_JEU_GENRE, batchArgs);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du jeu : {}", jeu, e);
            throw new DatabaseUpdateException("Erreur lors de la mise à jour du jeu.", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            // Suppression des genres associés et du jeu
            jdbcTemplate.update(DELETE_JEU_GENRE, id);
            int nbRows = jdbcTemplate.update(DELETE_JEU, id);
            if (nbRows != 1) {
                throw new JeuNotFoundException("La suppression du jeu a échouée, ID : " + id);
            }
            logger.info("Jeu supprimé avec succès, ID : {}", id);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du jeu avec l'id : {}", id, e);
            throw new DatabaseUpdateException("Erreur lors de la suppression du jeu.", e);
        }
    }

    @Override
    public List<Genre> getGenresByJeuId(Integer jeuId) {
        return jdbcTemplate.query(SELECT_GENRES_BY_JEU_ID, new BeanPropertyRowMapper<>(Genre.class), jeuId);
    }
}
