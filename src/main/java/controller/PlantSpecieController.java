package controller;



import reqResp.CreatePlantSpecieRequest;
import dto.PlantSpecieDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import model.PlantSpecie;
import org.springframework.web.bind.annotation.*;
import repository.PlantSpecieRepository;

import java.util.List;



@RestController
@RequestMapping("/api/species")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantSpecieController {

    private final EntityManagerFactory entityManagerFactory;

    public PlantSpecieController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @GetMapping
    public List<PlantSpecieDTO> getAllSpecies() {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            PlantSpecieRepository repository = new PlantSpecieRepository(em);

            return repository.findAll()
                    .stream()
                    .map(PlantSpecieDTO::fromEntity)
                    .toList();

        } finally {
            em.close();
        }
    }

    @GetMapping("/{id}")
    public PlantSpecieDTO getSpecieById(@PathVariable Integer id) {
        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            PlantSpecie specie = em.find(PlantSpecie.class, id);

            if (specie == null) {
                throw new IllegalArgumentException(
                        "Specie non trovata con id: " + id
                );
            }

            return PlantSpecieDTO.fromEntity(specie);

        } finally {
            em.close();
        }
    }

    @PostMapping
    public PlantSpecieDTO createSpecie(@RequestBody CreatePlantSpecieRequest request) {

        validateCreateSpecieRequest(request);

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            PlantSpecie existing = em.createQuery(
                            "SELECT ps FROM PlantSpecie ps WHERE LOWER(ps.nome) = LOWER(:nome)",
                            PlantSpecie.class
                    )
                    .setParameter("nome", request.getNome())
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                throw new IllegalArgumentException(
                        "Esiste già una specie con nome: " + request.getNome()
                );
            }

            PlantSpecie specie = new PlantSpecie();
            specie.setNome(request.getNome());
            specie.setTempBase(request.getTempBase());

            specie.setGddEmergenza(request.getGddEmergenza());
            specie.setGddSviluppo(request.getGddSviluppo());
            specie.setGddFioritura(request.getGddFioritura());
            specie.setGddFruttificazione(request.getGddFruttificazione());
            specie.setGddMaturazione(request.getGddMaturazione());

            specie.setSogliaStressCaldo(request.getSogliaStressCaldo());
            specie.setSogliaStressFreddo(request.getSogliaStressFreddo());

            specie.setSensibilitaMalattie(request.getSensibilitaMalattie());
            specie.setNoteAgronomiche(request.getNoteAgronomiche());

            em.persist(specie);
            em.flush();

            tx.commit();

            return PlantSpecieDTO.fromEntity(specie);

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    private void validateCreateSpecieRequest(CreatePlantSpecieRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new IllegalArgumentException("Il nome della specie è obbligatorio");
        }

        if (request.getTempBase() == null) {
            throw new IllegalArgumentException("La temperatura base è obbligatoria");
        }

        if (request.getGddEmergenza() == null ||
                request.getGddSviluppo() == null ||
                request.getGddFioritura() == null ||
                request.getGddFruttificazione() == null ||
                request.getGddMaturazione() == null) {
            throw new IllegalArgumentException("Le soglie GDD sono obbligatorie");
        }

        if (request.getSogliaStressCaldo() == null ||
                request.getSogliaStressFreddo() == null) {
            throw new IllegalArgumentException("Le soglie di stress caldo/freddo sono obbligatorie");
        }

        if (request.getGddEmergenza() < 0 ||
                request.getGddSviluppo() < 0 ||
                request.getGddFioritura() < 0 ||
                request.getGddFruttificazione() < 0 ||
                request.getGddMaturazione() < 0) {
            throw new IllegalArgumentException("Le soglie GDD non possono essere negative");
        }

        if (!(request.getGddEmergenza() <= request.getGddSviluppo()
                && request.getGddSviluppo() <= request.getGddFioritura()
                && request.getGddFioritura() <= request.getGddFruttificazione()
                && request.getGddFruttificazione() <= request.getGddMaturazione())) {
            throw new IllegalArgumentException(
                    "Le soglie GDD devono essere in ordine crescente"
            );
        }
    }
}
