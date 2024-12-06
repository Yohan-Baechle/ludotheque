package fr.eni.ludotheque.dal;

import fr.eni.ludotheque.bo.Location;
import fr.eni.ludotheque.bo.ExemplaireJeu;
import fr.eni.ludotheque.bo.Jeu;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LocationRepositoryImpl implements LocationRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public LocationRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(Location location) {
        String sql = "INSERT INTO locations (exemplaire_id, date_debut, date_fin, retour) " +
                     "VALUES (:exemplaire.id, :dateDebut, :dateFin, :retour)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(location), keyHolder, new String[]{"id"});
        location.setId(keyHolder.getKeyAs(Integer.class));
    }

    @Override
    public List<Location> findAll() {
        String sql = "SELECT l.id, l.date_debut AS dateDebut, l.date_fin AS dateFin, l.retour, " +
                     "e.id AS exemplaireId, e.code_barre AS codeBarre, " +
                     "j.id AS jeuId, j.titre AS jeuTitre, j.tarif_journee AS tarifJournee " +
                     "FROM locations l " +
                     "JOIN exemplaires_jeux e ON l.exemplaire_id = e.id " +
                     "JOIN jeux j ON e.jeu_id = j.id";
        return namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> {
            Location location = new Location();
            location.setId(rs.getInt("id"));
            location.setDateDebut(rs.getObject("dateDebut", LocalDate.class));
            location.setDateFin(rs.getObject("dateFin", LocalDate.class));
            location.setRetour(rs.getBoolean("retour"));

            ExemplaireJeu exemplaire = new ExemplaireJeu();
            exemplaire.setId(rs.getInt("exemplaireId"));
            exemplaire.setCodeBarre(rs.getString("codeBarre"));

            Jeu jeu = new Jeu();
            jeu.setId(rs.getInt("jeuId"));
            jeu.setTitre(rs.getString("jeuTitre"));
            jeu.setTarifJournee(rs.getDouble("tarifJournee"));

            exemplaire.setJeu(jeu);
            location.setExemplaire(exemplaire);

            return location;
        });
    }


    @Override
    public Optional<Location> findById(int id) {
        String sql = "SELECT l.id, l.date_debut AS dateDebut, l.date_fin AS dateFin, l.retour, " +
                     "e.id AS exemplaireId, e.code_barre AS codeBarre " +
                     "FROM locations l " +
                     "JOIN exemplaires_jeux e ON l.exemplaire_id = e.id " +
                     "WHERE l.id = :id";
        try {
            Location location = namedParameterJdbcTemplate.queryForObject(sql,
                    new BeanPropertySqlParameterSource(Map.of("id", id)),
                    (rs, rowNum) -> {
                        Location loc = new Location();
                        loc.setId(rs.getInt("id"));
                        loc.setDateDebut(rs.getObject("dateDebut", LocalDate.class));
                        loc.setDateFin(rs.getObject("dateFin", LocalDate.class));
                        loc.setRetour(rs.getBoolean("retour"));

                        ExemplaireJeu exemplaire = new ExemplaireJeu();
                        exemplaire.setId(rs.getInt("exemplaireId"));
                        exemplaire.setCodeBarre(rs.getString("codeBarre"));
                        loc.setExemplaire(exemplaire);

                        return loc;
                    });
            return Optional.of(location);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Location> findByExemplaireId(int exemplaireId) {
        String sql = "SELECT * FROM locations WHERE exemplaire_id = :exemplaireId";
        return namedParameterJdbcTemplate.query(sql,
                new BeanPropertySqlParameterSource(Map.of("exemplaireId", exemplaireId)),
                new BeanPropertyRowMapper<>(Location.class));
    }

    @Override
    public void update(Location location) {
        String sql = "UPDATE locations SET date_debut = :dateDebut, date_fin = :dateFin, retour = :retour, exemplaire_id = :exemplaire.id WHERE id = :id";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(location));
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM locations WHERE id = :id";
        Map<String, Object> params = Map.of("id", id);
        namedParameterJdbcTemplate.update(sql, params); // Passez directement la Map
    }
}
