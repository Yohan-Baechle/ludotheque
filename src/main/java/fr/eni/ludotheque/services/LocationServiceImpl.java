package fr.eni.ludotheque.services;

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
    public void add(Location location) {
        locationRepository.add(location);
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Optional<Location> findById(int id) {
        return locationRepository.findById(id);
    }

    @Override
    public void update(Location location) {
        locationRepository.update(location);
    }

    @Override
    public void delete(int id) {
        locationRepository.delete(id);
    }

	@Override
	public void save(Location location) {
		if (location.getId() == null) {
			this.add(location);
		} else {
			this.update(location);
		}
	}

	@Override
	public double calculerPrixTotal(LocalDate dateDebut, LocalDate dateFin, double tarifJournalier) {
	    if (dateDebut == null || tarifJournalier <= 0) {
	        return 0.0;
	    }
	    long jours = (dateFin != null)
	                 ? ChronoUnit.DAYS.between(dateDebut, dateFin)
	                 : ChronoUnit.DAYS.between(dateDebut, LocalDate.now());
	    return Math.max(jours, 1) * tarifJournalier; // Minimum d'un jour
	}
}
