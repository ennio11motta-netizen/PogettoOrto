package controller;

import exception.GrowthStage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import model.Location;
import model.PlantInstance;
import model.PlantSpecie;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "http://localhost:5173")
public class DemoDataController {

    private final EntityManagerFactory entityManagerFactory;

    public DemoDataController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @PostMapping("/init")
    public Map<String, Object> initDemoData() {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Location location = new Location();
            location.setNome("Magione");
            location.setLatitudine(43.1417);
            location.setLongitudine(12.2056);
            em.persist(location);

            PlantSpecie specie = new PlantSpecie();
            specie.setNome("Pomodoro");
            specie.setTempBase(10.0);

            specie.setGddEmergenza(80.0);
            specie.setGddSviluppo(350.0);
            specie.setGddFioritura(650.0);
            specie.setGddFruttificazione(950.0);
            specie.setGddMaturazione(1300.0);

            specie.setSogliaStressCaldo(32.0);
            specie.setSogliaStressFreddo(8.0);
            specie.setSensibilitaMalattie("Media");
            specie.setNoteAgronomiche("Pianta demo per test simulazione");
            em.persist(specie);

            PlantInstance plant = new PlantInstance();
            plant.setPlantSpecie(specie);
            plant.setDataInsert(LocalDateTime.now());
            plant.setStoreGDD(0.0);
            plant.setGrowthStage(GrowthStage.SEMINA);
            plant.setNote("Pianta demo creata da DemoDataController");
            em.persist(plant);

            em.flush();
            tx.commit();

            return Map.of(
                    "message", "Dati demo creati correttamente",
                    "locationId", location.getLocationId(),
                    "specieId", specie.getSpecieId(),
                    "plantId", plant.getPlantId()
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
}