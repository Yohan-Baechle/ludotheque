package fr.eni.ludotheque.dal;

import java.util.List;

import fr.eni.ludotheque.bo.ExemplaireJeu;

public interface ExemplaireJeuRepository extends ICrudRepository<ExemplaireJeu>{
	  List<ExemplaireJeu> findByJeuId(int jeuId);
}
