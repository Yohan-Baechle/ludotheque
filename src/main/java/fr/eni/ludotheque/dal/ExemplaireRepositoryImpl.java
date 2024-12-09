package fr.eni.ludotheque.dal;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.ludotheque.bo.Exemplaire;
import fr.eni.ludotheque.bo.Jeu;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;

@Repository
@Primary
public class ExemplaireRepositoryImpl implements ExemplaireRepository {

    private static final Logger logger = LoggerFactory.getLogger(ExemplaireRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    // Constantes SQL
    private static final String INSERT_EXEMPLAIRE = "INSERT INTO exemplaires_jeux (code_barre, louable, jeu_id) "
                                                       + "VALUES (:codeBarre, :louable, :jeu.id)";
    private static final String SELECT_ALL_EXEMPLAIRES = "SELECT ej.id, ej.code_barre AS codeBarre, ej.louable, "
                                                          + "j.id AS jeuId, j.titre AS jeuTitre "
                                                          + "FROM exemplaires_jeux ej "
                                                          + "LEFT JOIN jeux j ON ej.jeu_id = j.id";
    private static final String SELECT_EXEMPLAIRE_BY_ID = "SELECT ej.id, ej.code_barre AS codeBarre, ej.louable, "
                                                           + "j.id AS jeuId, j.titre AS jeuTitre "
                                                           + "FROM exemplaires_jeux ej "
                                                           + "LEFT JOIN jeux j ON ej.jeu_id = j.id "
                                                           + "WHERE ej.id = ?";
    private static final String SELECT_EXEMPLAIRES_BY_JEU_ID = "SELECT ej.id, ej.code_barre AS codeBarre, ej.louable, "
                                                                + "j.id AS jeuId, j.titre AS jeuTitre "
                                                                + "FROM exemplaires_jeux ej "
                                                                + "LEFT JOIN jeux j ON ej.jeu_id = j.id "
                                                                + "WHERE ej.jeu_id = ?";
    private static final String DELETE_EXEMPLAIRE = "DELETE FROM exemplaires_jeux WHERE id = ?";
    private static final String UPDATE_EXEMPLAIRE = "UPDATE exemplaires_jeux SET code_barre = :codeBarre, louable = :louable, jeu_id = :jeu.id "
                                                      + "WHERE id = :id";

    public ExemplaireRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(Exemplaire exemplaireJeu) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(INSERT_EXEMPLAIRE, new BeanPropertySqlParameterSource(exemplaireJeu), keyHolder, new String[]{"id"});
            exemplaireJeu.setId(keyHolder.getKeyAs(Integer.class));
            logger.debug("Exemplaire inséré avec succès, ID : {}", exemplaireJeu.getId());
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de l'exemplaire : {}", exemplaireJeu, e);
            throw new DatabaseUpdateException("Échec de l'ajout de l'exemplaire.", e);
        }
    }

    @Override
    public List<Exemplaire> findAll() {
        try {
            return jdbcTemplate.query(SELECT_ALL_EXEMPLAIRES, (rs, rowNum) -> {
                Exemplaire exemplaire = new Exemplaire();
                exemplaire.setId(rs.getInt("id"));
                exemplaire.setCodeBarre(rs.getString("codeBarre"));
                exemplaire.setLouable(rs.getBoolean("louable"));

                Jeu jeu = new Jeu();
                jeu.setId(rs.getInt("jeuId"));
                jeu.setTitre(rs.getString("jeuTitre"));

                exemplaire.setJeu(jeu);
                return exemplaire;
            });
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des exemplaires", e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des exemplaires.", e);
        }
    }

    @Override
    public Optional<Exemplaire> findById(int id) {
        try {
            Exemplaire exemplaire = jdbcTemplate.queryForObject(SELECT_EXEMPLAIRE_BY_ID, (rs, rowNum) -> {
                Exemplaire ex = new Exemplaire();
                ex.setId(rs.getInt("id"));
                ex.setCodeBarre(rs.getString("codeBarre"));
                ex.setLouable(rs.getBoolean("louable"));

                Jeu jeu = new Jeu();
                jeu.setId(rs.getInt("jeuId"));
                jeu.setTitre(rs.getString("jeuTitre"));

                ex.setJeu(jeu);
                return ex;
            }, id);
            return Optional.ofNullable(exemplaire);
        } catch (Exception e) {
            logger.warn("Exemplaire introuvable avec l'ID {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Exemplaire> findByJeuId(int jeuId) {
        try {
            return jdbcTemplate.query(SELECT_EXEMPLAIRES_BY_JEU_ID, (rs, rowNum) -> {
                Exemplaire exemplaire = new Exemplaire();
                exemplaire.setId(rs.getInt("id"));
                exemplaire.setCodeBarre(rs.getString("codeBarre"));
                exemplaire.setLouable(rs.getBoolean("louable"));

                Jeu jeu = new Jeu();
                jeu.setId(rs.getInt("jeuId"));
                jeu.setTitre(rs.getString("jeuTitre"));

                exemplaire.setJeu(jeu);
                return exemplaire;
            }, jeuId);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des exemplaires pour le jeu avec l'ID {}", jeuId, e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des exemplaires pour le jeu.", e);
        }
    }

    @Override
    public void update(Exemplaire exemplaireJeu) {
        try {
            int nbRows = namedParameterJdbcTemplate.update(UPDATE_EXEMPLAIRE, new BeanPropertySqlParameterSource(exemplaireJeu));
            if (nbRows != 1) {
                throw new RuntimeException("La mise à jour de l'exemplaire a échoué : " + exemplaireJeu);
            }
            logger.debug("Exemplaire mis à jour avec succès, ID : {}", exemplaireJeu.getId());
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de l'exemplaire : {}", exemplaireJeu, e);
            throw new DatabaseUpdateException("Erreur lors de la mise à jour de l'exemplaire.", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int nbRows = jdbcTemplate.update(DELETE_EXEMPLAIRE, id);
            if (nbRows != 1) {
                throw new RuntimeException("La suppression de l'exemplaire a échoué, ID : " + id);
            }
            logger.debug("Exemplaire supprimé avec succès, ID : {}", id);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de l'exemplaire avec l'ID : {}", id, e);
            throw new DatabaseUpdateException("Erreur lors de la suppression de l'exemplaire.", e);
        }
    }
}
