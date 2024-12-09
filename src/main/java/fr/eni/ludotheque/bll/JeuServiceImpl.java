package fr.eni.ludotheque.bll;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.bo.Jeu;
import fr.eni.ludotheque.dal.JeuRepository;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;
import fr.eni.ludotheque.exceptions.JeuNotFoundException;

@Service
public class JeuServiceImpl implements JeuService {

    private static final Logger logger = LoggerFactory.getLogger(JeuServiceImpl.class);

    private final JeuRepository jeuRepository;

    public JeuServiceImpl(JeuRepository jeuRepository) {
        this.jeuRepository = jeuRepository;
    }

    @Override
    public void create(Jeu jeu) {
        try {
            jeuRepository.add(jeu);
            logger.info("Jeu ajouté avec succès : {}", jeu);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du jeu : {}", jeu, e);
            throw new DatabaseUpdateException("Échec de l'ajout du jeu.", e);
        }
    }

    @Override
    public List<Jeu> findAll() {
        try {
            List<Jeu> jeux = jeuRepository.findAll();

            // Charger les genres pour chaque jeu
            for (Jeu jeu : jeux) {
                List<Genre> genres = jeuRepository.getGenresByJeuId(jeu.getId());
                jeu.setGenres(genres);
            }
            return jeux;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des jeux", e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des jeux.", e);
        }
    }

    @Override
    public Optional<Jeu> findById(int id) {
        try {
            Optional<Jeu> jeuOpt = jeuRepository.findById(id);

            // Charger les genres si le jeu existe
            jeuOpt.ifPresent(jeu -> {
                List<Genre> genres = jeuRepository.getGenresByJeuId(jeu.getId());
                jeu.setGenres(genres);
            });

            return jeuOpt;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du jeu avec l'ID : {}", id, e);
            throw new DatabaseUpdateException("Erreur lors de la récupération du jeu.", e);
        }
    }

    @Override
    public void delete(int id) {
        Optional<Jeu> jeuOpt = findById(id);
        if (jeuOpt.isPresent()) {
            try {
                jeuRepository.delete(id);
                logger.info("Jeu supprimé avec succès, ID : {}", id);
            } catch (Exception e) {
                logger.error("Erreur lors de la suppression du jeu avec l'ID : {}", id, e);
                throw new DatabaseUpdateException("Erreur lors de la suppression du jeu.", e);
            }
        } else {
            logger.error("Jeu introuvable lors de la suppression, ID : {}", id);
            throw new JeuNotFoundException("Jeu introuvable avec l'ID : " + id);
        }
    }

    @Override
    public void saveOrUpdate(Jeu jeu) {
        if (jeu.getId() == null) {
            this.create(jeu);  // Création si l'ID est null
        } else {
            Optional<Jeu> jeuOpt = findById(jeu.getId());
            if (jeuOpt.isPresent()) {
                try {
                    jeuRepository.update(jeu);
                    logger.info("Jeu mis à jour avec succès : {}", jeu);
                } catch (Exception e) {
                    logger.error("Erreur lors de la mise à jour du jeu : {}", jeu, e);
                    throw new DatabaseUpdateException("Erreur lors de la mise à jour du jeu.", e);
                }
            } else {
                logger.error("Jeu introuvable lors de la mise à jour, ID : {}", jeu.getId());
                throw new JeuNotFoundException("Jeu introuvable avec l'ID : " + jeu.getId());
            }
        }
    }
}
