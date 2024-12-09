package fr.eni.ludotheque.dal;

import fr.eni.ludotheque.bo.Location;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;
import fr.eni.ludotheque.bo.Exemplaire;
import fr.eni.ludotheque.bo.Jeu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(Location.class);

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Constantes SQL
    private static final String INSERT_LOCATION = "INSERT INTO locations (exemplaire_id, date_debut, date_fin, retour) " +
                                                 "VALUES (:exemplaire.id, :dateDebut, :dateFin, :retour)";
    private static final String SELECT_ALL_LOCATIONS = "SELECT l.id, l.date_debut AS dateDebut, l.date_fin AS dateFin, l.retour, " +
                                                      "e.id AS exemplaireId, e.code_barre AS codeBarre, " +
                                                      "j.id AS jeuId, j.titre AS jeuTitre, j.tarif_journee AS tarifJournee " +
                                                      "FROM locations l " +
                                                      "JOIN exemplaires_jeux e ON l.exemplaire_id = e.id " +
                                                      "JOIN jeux j ON e.jeu_id = j.id";
    private static final String SELECT_LOCATION_BY_ID = "SELECT l.id, l.date_debut AS dateDebut, l.date_fin AS dateFin, l.retour, " +
                                                       "e.id AS exemplaireId, e.code_barre AS codeBarre " +
                                                       "FROM locations l " +
                                                       "JOIN exemplaires_jeux e ON l.exemplaire_id = e.id " +
                                                       "WHERE l.id = :id";
    private static final String SELECT_LOCATIONS_BY_EXEMPLAIRE_ID = "SELECT l.id, l.date_debut AS dateDebut, l.date_fin AS dateFin, l.retour, " +
                                                                   "e.id AS exemplaireId, e.code_barre AS codeBarre, " +
                                                                   "j.id AS jeuId, j.titre AS jeuTitre, j.tarif_journee AS tarifJournee " +
                                                                   "FROM locations l " +
                                                                   "JOIN exemplaires_jeux e ON l.exemplaire_id = e.id " +
                                                                   "JOIN jeux j ON e.jeu_id = j.id " +
                                                                   "WHERE l.exemplaire_id = :exemplaireId";
    private static final String UPDATE_LOCATION = "UPDATE locations SET date_debut = :dateDebut, date_fin = :dateFin, retour = :retour, exemplaire_id = :exemplaire.id WHERE id = :id";
    private static final String DELETE_LOCATION = "DELETE FROM locations WHERE id = :id";

    public LocationRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(Location location) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(INSERT_LOCATION, new BeanPropertySqlParameterSource(location), keyHolder, new String[]{"id"});
            location.setId(keyHolder.getKeyAs(Integer.class));
        } catch (Exception e) {
            throw new DatabaseUpdateException("Échec de l'ajout de la location.", e);
        }
    }

    @Override
    public List<Location> findAll() {
        try {
            return namedParameterJdbcTemplate.query(SELECT_ALL_LOCATIONS, (rs, rowNum) -> {
                Location location = new Location();
                location.setId(rs.getInt("id"));
                location.setDateDebut(rs.getObject("dateDebut", LocalDate.class));
                location.setDateFin(rs.getObject("dateFin", LocalDate.class));
                location.setRetour(rs.getBoolean("retour"));

                Exemplaire exemplaire = new Exemplaire();
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
        } catch (Exception e) {
            throw new DatabaseUpdateException("Erreur lors de la récupération des locations.", e);
        }
    }

    @Override
    public Optional<Location> findById(int id) {
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(
                "SELECT l.id, l.date_debut AS dateDebut, l.date_fin AS dateFin, l.retour, " +
                "e.id AS exemplaireId, e.code_barre AS codeBarre, " +
                "j.id AS jeuId, j.titre AS jeuTitre, j.tarif_journee AS tarifJournee " +
                "FROM locations l " +
                "JOIN exemplaires_jeux e ON l.exemplaire_id = e.id " +
                "JOIN jeux j ON e.jeu_id = j.id " +
                "WHERE l.id = :id",
                Map.of("id", id),
                (rs, rowNum) -> {
                    Location location = new Location();
                    location.setId(rs.getInt("id"));
                    location.setDateDebut(rs.getObject("dateDebut", LocalDate.class));
                    location.setDateFin(rs.getObject("dateFin", LocalDate.class));
                    location.setRetour(rs.getBoolean("retour"));

                    Exemplaire exemplaire = new Exemplaire();
                    exemplaire.setId(rs.getInt("exemplaireId"));
                    exemplaire.setCodeBarre(rs.getString("codeBarre"));

                    Jeu jeu = new Jeu();
                    jeu.setId(rs.getInt("jeuId"));
                    jeu.setTitre(rs.getString("jeuTitre"));
                    jeu.setTarifJournee(rs.getDouble("tarifJournee"));

                    // Logs pour vérifier chaque étape
                    logger.debug("Location ID: {}, Date début: {}, Date fin: {}", 
                        location.getId(), location.getDateDebut(), location.getDateFin());
                    logger.debug("Exemplaire ID: {}, Code barre: {}", exemplaire.getId(), exemplaire.getCodeBarre());
                    logger.debug("Jeu ID: {}, Titre: {}, Tarif: {}", jeu.getId(), jeu.getTitre(), jeu.getTarifJournee());

                    exemplaire.setJeu(jeu);
                    location.setExemplaire(exemplaire);

                    return location;
                }
            ));
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la location avec l'ID : {}", id, e);
            return Optional.empty();
        }
    }



    @Override
    public List<Location> findByExemplaireId(int exemplaireId) {
        try {
            return namedParameterJdbcTemplate.query(SELECT_LOCATIONS_BY_EXEMPLAIRE_ID, new BeanPropertySqlParameterSource(Map.of("exemplaireId", exemplaireId)),
                    (rs, rowNum) -> {
                        Location location = new Location();
                        location.setId(rs.getInt("id"));
                        location.setDateDebut(rs.getObject("dateDebut", LocalDate.class));
                        location.setDateFin(rs.getObject("dateFin", LocalDate.class));
                        location.setRetour(rs.getBoolean("retour"));

                        Exemplaire exemplaire = new Exemplaire();
                        exemplaire.setId(rs.getInt("exemplaireId"));
                        exemplaire.setCodeBarre(rs.getString("codeBarre"));
                        location.setExemplaire(exemplaire);

                        Jeu jeu = new Jeu();
                        jeu.setId(rs.getInt("jeuId"));
                        jeu.setTitre(rs.getString("jeuTitre"));
                        jeu.setTarifJournee(rs.getDouble("tarifJournee"));

                        exemplaire.setJeu(jeu);
                        location.setExemplaire(exemplaire);

                        return location;
                    });
        } catch (Exception e) {
            throw new DatabaseUpdateException("Erreur lors de la récupération des locations pour l'exemplaire avec l'ID " + exemplaireId, e);
        }
    }

    @Override
    public void update(Location location) {
        try {
            int nbRows = namedParameterJdbcTemplate.update(UPDATE_LOCATION, new BeanPropertySqlParameterSource(location));
            if (nbRows != 1) {
                throw new RuntimeException("La mise à jour de la location a échoué.");
            }
        } catch (Exception e) {
            throw new DatabaseUpdateException("Erreur lors de la mise à jour de la location.", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            int nbRows = namedParameterJdbcTemplate.update(DELETE_LOCATION, Map.of("id", id));
            if (nbRows != 1) {
                throw new RuntimeException("La suppression de la location a échoué.");
            }
        } catch (Exception e) {
            throw new DatabaseUpdateException("Erreur lors de la suppression de la location.", e);
        }
    }
}
