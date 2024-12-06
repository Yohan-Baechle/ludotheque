package fr.eni.ludotheque.dal;

import java.util.List;

import fr.eni.ludotheque.bo.Genre;
import fr.eni.ludotheque.bo.Jeu;

public interface JeuRepository extends ICrudRepository<Jeu>{
	List<Genre> getGenresByJeuId(Integer id);
}
