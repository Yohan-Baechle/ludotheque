package fr.eni.ludotheque.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eni.ludotheque.bo.ExemplaireJeu;
import fr.eni.ludotheque.dal.ExemplaireJeuRepository;

@Service
public class ExemplaireJeuServiceImpl implements ExemplaireJeuService {

    private final ExemplaireJeuRepository exemplaireJeuRepository;

    public ExemplaireJeuServiceImpl(ExemplaireJeuRepository exemplaireJeuRepository) {
        this.exemplaireJeuRepository = exemplaireJeuRepository;
    }

    @Override
    public void add(ExemplaireJeu exemplaireJeu) {
        exemplaireJeuRepository.add(exemplaireJeu);
    }

    @Override
    public Optional<ExemplaireJeu> findById(int id) {
        return exemplaireJeuRepository.findById(id);
    }

    @Override
    public List<ExemplaireJeu> findAll() {
        return exemplaireJeuRepository.findAll();
    }

    @Override
    public void update(ExemplaireJeu exemplaireJeu) {
        exemplaireJeuRepository.update(exemplaireJeu);
    }

    @Override
    public void delete(int id) {
        exemplaireJeuRepository.delete(id);
    }

	@Override
	public void save(ExemplaireJeu exemplaireJeu) {
		if (exemplaireJeu.getId() == null) {
			this.add(exemplaireJeu);
		} else {
			this.update(exemplaireJeu);
		}
	}
}
