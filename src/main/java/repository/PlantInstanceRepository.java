package repository;
import model.Location;
import model.PlantInstance;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.PlantSpecie;
import java.util.Optional;
public class PlantInstanceRepository {

    private final EntityManager em;

    public PlantInstanceRepository(EntityManager em) {
        this.em = em;
    }

    // ===============================
    // SAVE
    // ===============================
    public PlantInstance save(PlantInstance pianta) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(pianta);
            tx.commit();
            return pianta;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
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
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            PlantInstance updated = em.merge(pianta);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // ===============================
    // DELETE
    // ===============================
    public void deleteById(Integer id) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            PlantInstance pianta = em.find(PlantInstance.class, id);
            if (pianta != null) em.remove(pianta);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
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