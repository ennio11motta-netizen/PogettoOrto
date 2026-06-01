
package service;

import exception.GrowthStage;
import model.Location;
import model.PlantInstance;
import model.PlantSpecie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.LocationRepository;
import repository.PlantInstanceRepository;
import repository.PlantSpecieRepository;
import reqResp.GardenSetupRequest;
import reqResp.GardenSetupResponse;

import java.time.LocalDateTime;

@Service
public class GardenSetupService {

    private final LocationRepository locationRepository;
    private final PlantInstanceRepository plantInstanceRepository;
    private final PlantSpecieRepository plantSpecieRepository;

    public GardenSetupService(
            LocationRepository locationRepository,
            PlantInstanceRepository plantInstanceRepository,
            PlantSpecieRepository plantSpecieRepository
    ) {
        this.locationRepository = locationRepository;
        this.plantInstanceRepository = plantInstanceRepository;
        this.plantSpecieRepository = plantSpecieRepository;
    }

    @Transactional
    public GardenSetupResponse setupGarden(GardenSetupRequest request) {
        validateRequest(request);

        PlantSpecie specie = plantSpecieRepository.findById(request.getSpecieId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Specie non trovata con id: " + request.getSpecieId()
                ));

        Location location = createLocation(request);
        Location savedLocation = locationRepository.save(location);

        PlantInstance plant = createPlant(request, specie, savedLocation);
        PlantInstance savedPlant = plantInstanceRepository.save(plant);

        return toResponse(savedLocation, savedPlant, specie);
    }

    private Location createLocation(GardenSetupRequest request) {
        Location location = new Location();
        location.setNome(request.getNomeOrto());
        location.setLatitudine(request.getLatitudine());
        location.setLongitudine(request.getLongitudine());
        return location;
    }

    private PlantInstance createPlant(
            GardenSetupRequest request,
            PlantSpecie specie,
            Location location
    ) {
        PlantInstance plant = new PlantInstance();
        plant.setNome(request.getNomePianta());
        plant.setPlantSpecie(specie);
        plant.setLocation(location);
        plant.setDataInsert(LocalDateTime.now());
        plant.setStoreGDD(0.0);
        plant.setGrowthStage(GrowthStage.SEMINA);
        plant.setNote(request.getNote());
        return plant;
    }

    private GardenSetupResponse toResponse(
            Location location,
            PlantInstance plant,
            PlantSpecie specie
    ) {
        return new GardenSetupResponse(
                location.getLocationId(),
                location.getNome(),
                plant.getPlantId(),
                plant.getNome(),
                specie.getSpecieId(),
                specie.getNome(),
                plant.getGrowthStage() != null ? plant.getGrowthStage().name() : null,
                plant.getStoreGDD()
        );
    }

    private void validateRequest(GardenSetupRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        if (request.getNomeOrto() == null || request.getNomeOrto().isBlank()) {
            throw new IllegalArgumentException("Il nome dell'orto è obbligatorio");
        }

        if (request.getLatitudine() == null) {
            throw new IllegalArgumentException("La latitudine è obbligatoria");
        }

        if (request.getLongitudine() == null) {
            throw new IllegalArgumentException("La longitudine è obbligatoria");
        }

        if (request.getLatitudine() < -90 || request.getLatitudine() > 90) {
            throw new IllegalArgumentException("Latitudine non valida");
        }

        if (request.getLongitudine() < -180 || request.getLongitudine() > 180) {
            throw new IllegalArgumentException("Longitudine non valida");
        }

        if (request.getSpecieId() == null) {
            throw new IllegalArgumentException("La specie è obbligatoria");
        }

        if (request.getNomePianta() == null || request.getNomePianta().isBlank()) {
            throw new IllegalArgumentException("Il nome della pianta è obbligatorio");
        }
    }
}