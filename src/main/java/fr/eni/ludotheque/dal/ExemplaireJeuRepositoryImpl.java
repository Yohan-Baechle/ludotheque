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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.ludotheque.bo.ExemplaireJeu;
import fr.eni.ludotheque.bo.Jeu;

@Repository
@Primary
public class ExemplaireJeuRepositoryImpl implements ExemplaireJeuRepository {

    Logger logger = LoggerFactory.getLogger(ExemplaireJeuRepositoryImpl.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public ExemplaireJeuRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = namedParameterJdbcTemplate.getJdbcTemplate();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(ExemplaireJeu exemplaireJeu) {
        String sql = "INSERT INTO exemplaires_jeux (code_barre, louable, jeu_id) "
                   + "VALUES (:codeBarre, :louable, :jeu.id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(exemplaireJeu), keyHolder, new String[]{"id"});
        exemplaireJeu.setId(keyHolder.getKeyAs(Integer.class));
        logger.debug("Exemplaire inséré avec succès, ID : {}", exemplaireJeu.getId());
    }



    @Override
    public List<ExemplaireJeu> findAll() {
        String sql = "SELECT ej.id, ej.code_barre AS codeBarre, ej.louable, "
                   + "j.id AS jeuId, j.titre AS jeuTitre "
                   + "FROM exemplaires_jeux ej "
                   + "LEFT JOIN jeux j ON ej.jeu_id = j.id";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ExemplaireJeu exemplaire = new ExemplaireJeu();
            exemplaire.setId(rs.getInt("id"));
            exemplaire.setCodeBarre(rs.getString("codeBarre"));
            exemplaire.setLouable(rs.getBoolean("louable"));

            Jeu jeu = new Jeu();
            jeu.setId(rs.getInt("jeuId"));
            jeu.setTitre(rs.getString("jeuTitre"));

            exemplaire.setJeu(jeu);
            return exemplaire;
        });
    }


    @Override
    public Optional<ExemplaireJeu> findById(int id) {
        String sql = "SELECT ej.id, ej.code_barre AS codeBarre, ej.louable, "
                   + "j.id AS jeuId, j.titre AS jeuTitre "
                   + "FROM exemplaires_jeux ej "
                   + "LEFT JOIN jeux j ON ej.jeu_id = j.id "
                   + "WHERE ej.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                ExemplaireJeu exemplaire = new ExemplaireJeu();
                exemplaire.setId(rs.getInt("id"));
                exemplaire.setCodeBarre(rs.getString("codeBarre"));
                exemplaire.setLouable(rs.getBoolean("louable"));

                Jeu jeu = new Jeu();
                jeu.setId(rs.getInt("jeuId"));
                jeu.setTitre(rs.getString("jeuTitre"));

                exemplaire.setJeu(jeu);
                return Optional.of(exemplaire);
            }, id);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'exemplaire avec l'ID {}", id, e);
            return Optional.empty();
        }
    }


    @Override
    public List<ExemplaireJeu> findByJeuId(int jeuId) {
        String sql = "SELECT id, code_barre AS codeBarre, louable, jeu_id AS jeuId "
                   + "FROM exemplaires_jeux WHERE jeu_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ExemplaireJeu.class), jeuId);
    }

    @Override
    public void update(ExemplaireJeu exemplaireJeu) {
        String sql = "UPDATE exemplaires_jeux SET code_barre = :codeBarre, louable = :louable, jeu_id = :jeu.id "
                   + "WHERE id = :id";
        int nbRows = namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(exemplaireJeu));
        if (nbRows != 1) {
            throw new RuntimeException("La mise à jour de l'exemplaire a échoué : " + exemplaireJeu);
        }
        logger.debug("Exemplaire mis à jour avec succès, ID : {}", exemplaireJeu.getId());
    }



    @Override
    public void delete(int id) {
        String sql = "DELETE FROM exemplaires_jeux WHERE id = ?";
        int nbRows = jdbcTemplate.update(sql, id);
        if (nbRows != 1) {
            throw new RuntimeException("La suppression de l'exemplaire a échoué, ID : " + id);
        }
        logger.debug("Exemplaire supprimé avec succès, ID : {}", id);
    }
}
