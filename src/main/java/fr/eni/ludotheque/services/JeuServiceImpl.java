package fr.eni.ludotheque.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.bo.Jeu;
import fr.eni.ludotheque.dal.JeuRepository;
import fr.eni.ludotheque.exceptions.JeuNotFoundException;

@Service
public class JeuServiceImpl implements JeuService {

    private final JeuRepository jeuRepository;

    public JeuServiceImpl(JeuRepository jeuRepository) {
        this.jeuRepository = jeuRepository;
    }

    @Override
    public void add(Jeu jeu) {
        jeuRepository.add(jeu);
    }

    @Override
    public List<Jeu> findAll() {
        // Récupère tous les jeux
        List<Jeu> jeux = jeuRepository.findAll();

        // Charge les genres pour chaque jeu
        for (Jeu jeu : jeux) {
            List<Genre> genres = jeuRepository.getGenresByJeuId(jeu.getId());
            jeu.setGenres(genres);
        }
        return jeux;
    }

    @Override
    public Optional<Jeu> findById(int id) {
        // Récupère un jeu par son ID
        Optional<Jeu> jeuOpt = jeuRepository.findById(id);

        // Charge les genres si le jeu existe
        jeuOpt.ifPresent(jeu -> {
            List<Genre> genres = jeuRepository.getGenresByJeuId(jeu.getId());
            jeu.setGenres(genres);
        });

        return jeuOpt;
    }

    @Override
    public void update(Jeu jeu) {
        Optional<Jeu> jeuOpt = findById(jeu.getId());
        if (jeuOpt.isPresent()) {
            jeuRepository.update(jeu);
        } else {
            // Gérer l'erreur si le jeu n'est pas trouvé
            throw new JeuNotFoundException();
        }
    }

    @Override
    public void delete(int id) {
        jeuRepository.delete(id);
    }

    @Override
    public void save(Jeu entity) {
        if (entity.getId() == null) {
            this.add(entity);
            return;
        }
        this.update(entity);
    }
}
