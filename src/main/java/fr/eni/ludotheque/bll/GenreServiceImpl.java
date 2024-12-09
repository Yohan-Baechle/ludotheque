package fr.eni.ludotheque.bll;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.dal.GenreRepository;
import fr.eni.ludotheque.exceptions.DatabaseUpdateException;
import fr.eni.ludotheque.exceptions.GenreNotFoundException;

@Service("genreService")
public class GenreServiceImpl implements GenreService {

    private static final Logger logger = LoggerFactory.getLogger(GenreServiceImpl.class);

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public void create(Genre genre) {
        try {
            genreRepository.add(genre);
            logger.info("Genre ajouté avec succès : {}", genre);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du genre : {}", genre, e);
            throw new DatabaseUpdateException("Échec de l'ajout du genre.", e);
        }
    }

    @Override
    public List<Genre> findAll() {
        try {
            return genreRepository.findAll();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la liste des genres", e);
            throw new DatabaseUpdateException("Erreur lors de la récupération des genres.", e);
        }
    }

    @Override
    public Optional<Genre> findById(int id) {
        return genreRepository.findById(id);
    }

    @Override
    public void delete(int id) {
        Optional<Genre> genreOpt = findById(id);
        if (genreOpt.isPresent()) {
            try {
                genreRepository.delete(id);
                logger.info("Genre supprimé avec succès, ID : {}", id);
            } catch (Exception e) {
                logger.error("Erreur lors de la suppression du genre avec l'id : {}", id, e);
                throw new DatabaseUpdateException("Erreur lors de la suppression du genre.", e);
            }
        } else {
            logger.error("Genre non trouvé lors de la suppression, ID : {}", id);
            throw new GenreNotFoundException("Genre introuvable avec l'ID : " + id);
        }
    }

    @Override
    public void saveOrUpdate(Genre genre) {
        if (genre.getId() == null) {
            this.create(genre);
        } else {
            Optional<Genre> genreOpt = findById(genre.getId());
            if (genreOpt.isPresent()) {
                try {
                    genreRepository.update(genre);
                    logger.info("Genre mis à jour avec succès : {}", genre);
                } catch (Exception e) {
                    logger.error("Erreur lors de la mise à jour du genre : {}", genre, e);
                    throw new DatabaseUpdateException("Erreur lors de la mise à jour du genre.", e);
                }
            } else {
                logger.error("Genre introuvable lors de la mise à jour, ID : {}", genre.getId());
                throw new GenreNotFoundException("Genre introuvable avec l'ID : " + genre.getId());
            }
        }
    }
}
