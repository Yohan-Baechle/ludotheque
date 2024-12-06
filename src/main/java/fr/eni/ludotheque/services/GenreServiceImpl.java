package fr.eni.ludotheque.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.dal.GenreRepository;
import fr.eni.ludotheque.exceptions.GenreNotFoundException;

@Service("genreService")
public class GenreServiceImpl implements GenreService {

	private final GenreRepository genreRepository;

	public GenreServiceImpl(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	@Override
	public void add(Genre genre) {
		genreRepository.add(genre);
	}

	@Override
	public List<Genre> findAll() {
		return genreRepository.findAll();
	}

	@Override
	public Optional<Genre> findById(int id) {
		return genreRepository.findById(id);
	}

	@Override
	public void update(Genre genre) {
		Optional<Genre> genreOpt = findById(genre.getId());
		if (genreOpt.isPresent()) {
			genreRepository.update(genre);
		} else {
			// Gestion de l'erreur : genre non trouvé
			throw new GenreNotFoundException();
		}
	}

	@Override
	public void delete(int id) {
		genreRepository.delete(id);
	}

	@Override
	public void save(Genre genre) {
		if (genre.getId() == null) {
			this.add(genre);
		} else {
			this.update(genre);
		}
	}
}
