package fr.eni.ludotheque.dal;

import java.util.List;

import fr.eni.ludotheque.bo.Exemplaire;

public interface ExemplaireRepository extends ICrudRepository<Exemplaire>{
	  List<Exemplaire> findByJeuId(int jeuId);
}
