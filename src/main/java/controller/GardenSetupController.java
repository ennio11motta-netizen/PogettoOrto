package controller;

import dto_mapper.GardenSetupRequest;
import dto_mapper.GardenSetupResponse;
import exception.GrowthStage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import model.Location;
import model.PlantInstance;
import model.PlantSpecie;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/garden")
@CrossOrigin(origins = "http://localhost:5173")
public class GardenSetupController {

    private final EntityManagerFactory entityManagerFactory;

    public GardenSetupController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @PostMapping("/setup")
    public GardenSetupResponse setupGarden(@RequestBody GardenSetupRequest request) {

        validateRequest(request);

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            PlantSpecie specie = em.find(PlantSpecie.class, request.getSpecieId());

            if (specie == null) {
                throw new IllegalArgumentException(
                        "Specie non trovata con id: " + request.getSpecieId()
                );
            }

            Location location = new Location();
            location.setNome(request.getNomeOrto());
            location.setLatitudine(request.getLatitudine());
            location.setLongitudine(request.getLongitudine());
            em.persist(location);

            PlantInstance plant = new PlantInstance();
            plant.setNome(request.getNomePianta());
            plant.setPlantSpecie(specie);
            plant.setLocation(location);
            plant.setDataInsert(LocalDateTime.now());
            plant.setStoreGDD(0.0);
            plant.setGrowthStage(GrowthStage.SEMINA);
            plant.setNote(request.getNote());

            em.persist(plant);

            em.flush();
            tx.commit();

            return new GardenSetupResponse(
                    location.getLocationId(),
                    location.getNome(),
                    plant.getPlantId(),
                    plant.getNome(),
                    specie.getSpecieId(),
                    specie.getNome(),
                    plant.getGrowthStage().name(),
                    plant.getStoreGDD()
            );

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private void validateRequest(GardenSetupRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        if (request.getNomeOrto() == null || request.getNomeOrto().isBlank()) {
            throw new IllegalArgumentException("Il nome dell'orto è obbligatorio");
        }

        if (request.getLatitudine() == null || request.getLongitudine() == null) {
            throw new IllegalArgumentException("Latitudine e longitudine sono obbligatorie");
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