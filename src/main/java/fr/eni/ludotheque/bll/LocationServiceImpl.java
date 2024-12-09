package fr.eni.ludotheque.bll;

import fr.eni.ludotheque.bo.Location;
import fr.eni.ludotheque.dal.LocationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void create(Location location) {
        locationRepository.add(location);
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Optional<Location> findById(int id) {
        return locationRepository.findById(id); // Retourne un Optional<Location> comme attendu
    }

    @Override
    public void delete(int id) {
        locationRepository.delete(id);
    }

    @Override
    public void saveOrUpdate(Location location) {
        if (location.getId() == null) {
            this.create(location);  // Si l'ID est nul, ajout du lieu
        } else {
            this.saveOrUpdate(location);  // Si l'ID existe, mise à jour du lieu
        }
    }
    
    @Override
    public double calculerPrixTotal(LocalDate dateDebut, LocalDate dateFin, double tarifJournalier) {
        long jours = dateFin != null 
                     ? ChronoUnit.DAYS.between(dateDebut, dateFin) 
                     : ChronoUnit.DAYS.between(dateDebut, LocalDate.now());
        return jours * tarifJournalier;
    }
}
