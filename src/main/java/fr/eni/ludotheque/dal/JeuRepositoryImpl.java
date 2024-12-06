package fr.eni.ludotheque.dal;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
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

@Repository
@Primary
public class JeuRepositoryImpl implements JeuRepository {
    Logger logger = LoggerFactory.getLogger(JeuRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public JeuRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    record JeuGenreDto(Integer jeuId, Integer genreId) {}

    @Override
    @Transactional
    public void add(Jeu newJeu) {
        logger.debug("avant insert into jeux...");
        String sql = "insert into jeux (titre, reference, description, tarif_journee, age_min, duree) "
                   + "values (:titre, :reference, :description, :tarifJournee, :ageMin, :duree)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(newJeu), keyHolder, new String[]{"id"});
        Integer jeuId = keyHolder.getKeyAs(Integer.class);
        logger.debug("apres insert into jeux...jeuId=" + jeuId);
        newJeu.setId(jeuId);

        List<JeuGenreDto> jeuGenreDtos = newJeu.getGenres().stream().map(genre -> new JeuGenreDto(jeuId, genre.getId())).toList();

        logger.debug("avant insert into jeux_genres...");
        sql = "insert into jeux_genres (jeu_id, genre_id) values (:jeuId, :genreId)";
        SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(jeuGenreDtos);
        this.namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
        logger.debug("après insert into jeux_genres...");
    }

    @Override
    public List<Jeu> findAll() {
        String sql = "select id, titre, reference, description, tarif_journee, age_min, duree from jeux";
        List<Jeu> jeux = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Jeu.class));
        return jeux;
    }

    @Override
    public Optional<Jeu> findById(int id) {
        String sql = "select id, titre, reference, description, tarif_journee, age_min, duree from jeux where id = ?";
        Jeu jeu = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Jeu.class), id);

        List<Genre> genres = getGenresByJeuId(jeu.getId());
        jeu.setGenres(genres);

        return Optional.ofNullable(jeu);
    }

    @Override
    public void update(Jeu jeu) {
        String sql = "update jeux set titre=:titre, reference=:reference, description=:description, "
                   + "tarif_journee=:tarifJournee, age_min=:ageMin, duree=:duree where id = :id";
        int nbRows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(jeu));
        if (nbRows != 1) {
            throw new RuntimeException("La modification du jeu a échouée : " + jeu);
        }

        // Suppression des genres existants
        sql = "delete from jeux_genres where jeu_id = ?";
        jdbcTemplate.update(sql, jeu.getId());

        // Ajout des nouveaux genres
        List<JeuGenreDto> jeuGenreDtos = jeu.getGenres().stream().map(genre -> new JeuGenreDto(jeu.getId(), genre.getId())).toList();
        sql = "insert into jeux_genres (jeu_id, genre_id) values (:jeuId, :genreId)";
        SqlParameterSource[] batchArgs = SqlParameterSourceUtils.createBatch(jeuGenreDtos);
        this.namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void delete(int id) {
        String sql = "delete from jeux_genres where jeu_id = ?";
        jdbcTemplate.update(sql, id);

        sql = "delete from jeux where id = ?";
        int nbRows = jdbcTemplate.update(sql, id);
        if (nbRows != 1) {
            throw new RuntimeException("La suppression du jeu a échouée : id=" + id);
        }
    }

    @Override
    public List<Genre> getGenresByJeuId(Integer jeuId) {
        String sql = "select genres.id, genres.libelle "
                   + "from jeux_genres inner join genres on jeux_genres.genre_id = genres.id "
                   + "where jeux_genres.jeu_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Genre.class), jeuId);
    }
}
