package controller;



import reqResp.CreatePlantRequest;
import dto.PlantDTO;
import exception.GrowthStage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import model.Location;
import model.PlantInstance;
import model.PlantSpecie;
import org.springframework.web.bind.annotation.*;
import repository.PlantInstanceRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantController {

    private final EntityManagerFactory entityManagerFactory;

    public PlantController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @PostMapping
    public PlantDTO createPlant(@RequestBody CreatePlantRequest request) {

        validateCreatePlantRequest(request);

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Location location = em.find(Location.class, request.getLocationId());
            if (location == null) {
                throw new IllegalArgumentException(
                        "Location non trovata con id: " + request.getLocationId()
                );
            }

            PlantSpecie specie = em.find(PlantSpecie.class, request.getSpecieId());
            if (specie == null) {
                throw new IllegalArgumentException(
                        "Specie non trovata con id: " + request.getSpecieId()
                );
            }

            PlantInstance plant = new PlantInstance();
            plant.setNome(request.getNomePianta());
            plant.setLocation(location);
            plant.setPlantSpecie(specie);
            plant.setDataInsert(LocalDateTime.now());
            plant.setStoreGDD(0.0);
            plant.setGrowthStage(GrowthStage.SEMINA);
            plant.setNote(request.getNote());

            em.persist(plant);

            em.flush();
            tx.commit();

            return PlantDTO.fromEntity(plant);

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @GetMapping
    public List<PlantDTO> getPlantsByLocation(@RequestParam Integer locationId) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Location location = em.find(Location.class, locationId);
            if (location == null) {
                throw new IllegalArgumentException(
                        "Location non trovata con id: " + locationId
                );
            }

            PlantInstanceRepository plantRepository =
                    new PlantInstanceRepository(em);

            return plantRepository.findByLocation(location)
                    .stream()
                    .map(PlantDTO::fromEntity)
                    .toList();

        } finally {
            em.close();
        }
    }

    @GetMapping("/{id}")
    public PlantDTO getPlantById(@PathVariable Integer id) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            PlantInstance plant = em.find(PlantInstance.class, id);

            if (plant == null) {
                throw new IllegalArgumentException(
                        "Pianta non trovata con id: " + id
                );
            }

            return PlantDTO.fromEntity(plant);

        } finally {
            em.close();
        }
    }

    private void validateCreatePlantRequest(CreatePlantRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        if (request.getLocationId() == null) {
            throw new IllegalArgumentException("La location è obbligatoria");
        }

        if (request.getSpecieId() == null) {
            throw new IllegalArgumentException("La specie è obbligatoria");
        }

        if (request.getNomePianta() == null || request.getNomePianta().isBlank()) {
            throw new IllegalArgumentException("Il nome della pianta è obbligatorio");
        }
    }
}