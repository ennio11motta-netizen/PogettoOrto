package repository;
import model.Location;
import model.PlantInstance;
import java.util.List;
import jakarta.persistence.EntityManager;
import model.PlantSpecie;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class PlantInstanceRepository {

    private final EntityManager em;

    public PlantInstanceRepository(EntityManager em) {
        this.em = em;
    }

    // ===============================
    // SAVE
    // ===============================
    @Transactional
    public PlantInstance save(PlantInstance pianta) {
        em.persist(pianta);
        return  pianta;

    }

    // ===============================
    // FIND BY ID
    // ===============================
    public Optional<PlantInstance> findById(Integer id) {
        return Optional.ofNullable(em.find(PlantInstance.class, id));
    }

    // ===============================
    // FIND ALL
    // ===============================
    public List<PlantInstance> findAll() {
        return em.createQuery(
                "SELECT pi FROM PlantInstance pi",
                PlantInstance.class
        ).getResultList();
    }

    // ===============================
    // FIND BY SPECIE
    // ===============================
    public List<PlantInstance> findBySpecie(PlantSpecie specie) {
        return em.createQuery(
                        "SELECT pi FROM PlantInstance pi WHERE pi.plantSpecie = :specie",
                        PlantInstance.class
                ).setParameter("specie", specie)
                .getResultList();
    }

    // ===============================
    // ORDINATI PER DATA
    // ===============================
    public List<PlantInstance> findAllOrderByDataAsc() {
        return em.createQuery(
                "SELECT pi FROM PlantInstance pi ORDER BY pi.dataInsert ASC",
                PlantInstance.class
        ).getResultList();
    }

    // ===============================
    // UPDATE
    // ===============================
    public PlantInstance update(PlantInstance pianta) {
        return em.merge(pianta);

    }

    // ===============================
    // DELETE
    // ===============================
    public void deleteById(Integer id) {

        PlantInstance pianta = em.find(PlantInstance.class, id);

        if (pianta != null) {
            em.remove(pianta);
        }


    }

    //====================================
    //FIND BY LOCATION
    //===================================

    public List<PlantInstance> findByLocation(Location location) {
        return em.createQuery(
                        "SELECT pi FROM PlantInstance pi WHERE pi.location = :location ORDER BY pi.dataInsert ASC",
                        PlantInstance.class
                )
                .setParameter("location", location)
                .getResultList();
    }
}