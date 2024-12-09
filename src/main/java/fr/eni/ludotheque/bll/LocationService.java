package fr.eni.ludotheque.bll;

import fr.eni.ludotheque.bo.Location;

import java.time.LocalDate;

public interface LocationService extends ICrudService<Location> {
    double calculerPrixTotal(LocalDate dateDebut, LocalDate dateFin, double tarifJournalier);
}
