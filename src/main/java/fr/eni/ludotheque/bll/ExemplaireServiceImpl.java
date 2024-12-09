package fr.eni.ludotheque.bll;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.eni.ludotheque.bo.Exemplaire;
import fr.eni.ludotheque.dal.ExemplaireRepository;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;
import fr.eni.ludotheque.exceptions.ExemplaireJeuNotFoundException;

@Service
public class ExemplaireServiceImpl implements ExemplaireService {

    private static final Logger logger = LoggerFactory.getLogger(ExemplaireServiceImpl.class);

    private final ExemplaireRepository exemplaireJeuRepository;

    public ExemplaireServiceImpl(ExemplaireRepository exemplaireJeuRepository) {
        this.exemplaireJeuRepository = exemplaireJeuRepository;
    }

    @Override
    public void create(Exemplaire exemplaireJeu) {
        try {
            exemplaireJeuRepository.add(exemplaireJeu);
            logger.info("Exemplaire ajouté avec succès : {}", exemplaireJeu);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de l'exemplaire : {}", exemplaireJeu, e);
            throw new DatabaseUpdateException("Échec de l'ajout de l'exemplaire.", e);
        }
    }

    @Override
    public Optional<Exemplaire> findById(int id) {
        try {
            return exemplaireJeuRepository.findById(id);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'exemplaire avec l'ID : {}", id, e);
            throw new DatabaseUpdateException("Erreur lors de la récupération de l'exemplaire.", e);
        }
    }

    @Override
    public List<Exemplaire> findAll() {
        try {
            return exemplaireJeuRepository.findAll();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des exemplaires", e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des exemplaires.", e);
        }
    }

    @Override
    public void delete(int id) {
        Optional<Exemplaire> exemplaireOpt = findById(id);
        if (exemplaireOpt.isPresent()) {
            try {
                exemplaireJeuRepository.delete(id);
                logger.info("Exemplaire supprimé avec succès, ID : {}", id);
            } catch (Exception e) {
                logger.error("Erreur lors de la suppression de l'exemplaire avec l'ID : {}", id, e);
                throw new DatabaseUpdateException("Erreur lors de la suppression de l'exemplaire.", e);
            }
        } else {
            logger.error("Exemplaire non trouvé lors de la suppression, ID : {}", id);
            throw new ExemplaireJeuNotFoundException("Exemplaire introuvable avec l'ID : " + id);
        }
    }

    @Override
    public void saveOrUpdate(Exemplaire exemplaireJeu) {
        if (exemplaireJeu.getId() == null) {
            this.create(exemplaireJeu);  // Création si l'ID est null
        } else {
            Optional<Exemplaire> exemplaireOpt = findById(exemplaireJeu.getId());
            if (exemplaireOpt.isPresent()) {
                try {
                    exemplaireJeuRepository.update(exemplaireJeu);
                    logger.info("Exemplaire mis à jour avec succès : {}", exemplaireJeu);
                } catch (Exception e) {
                    logger.error("Erreur lors de la mise à jour de l'exemplaire : {}", exemplaireJeu, e);
                    throw new DatabaseUpdateException("Erreur lors de la mise à jour de l'exemplaire.", e);
                }
            } else {
                logger.error("Exemplaire introuvable lors de la mise à jour, ID : {}", exemplaireJeu.getId());
                throw new ExemplaireJeuNotFoundException("Exemplaire introuvable avec l'ID : " + exemplaireJeu.getId());
            }
        }
    }
}
