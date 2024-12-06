package fr.eni.ludotheque.dal;

import fr.eni.ludotheque.bo.Location;

import java.util.List;

public interface LocationRepository extends ICrudRepository<Location> {
    List<Location> findByExemplaireId(int exemplaireId);
}
