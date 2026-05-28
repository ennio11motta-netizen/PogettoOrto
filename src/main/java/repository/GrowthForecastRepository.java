package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.GrowthForecast;
import model.PlantInstance;
import java.util.List;
import java.util.Optional;

public class GrowthForecastRepository {

    private final EntityManager em;

    public GrowthForecastRepository(EntityManager em) {
        this.em = em;
    }

    // ===============================
    // SAVE
    // ===============================
    public GrowthForecast save(GrowthForecast gf) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(gf);
            tx.commit();
            return gf;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // ===============================
    // UPDATE
    // ===============================
    public GrowthForecast update(GrowthForecast gf) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            GrowthForecast updated = em.merge(gf);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // ===============================
    // FIND BY ID
    // ===============================
    public Optional<GrowthForecast> findById(Integer id) {
        GrowthForecast gf = em.find(GrowthForecast.class, id);
        return Optional.ofNullable(gf);
    }

    // ===============================
    // FIND ALL
    // ===============================
    public List<GrowthForecast> findAll() {
        return em.createQuery(
                "SELECT gf FROM GrowthForecast gf",
                GrowthForecast.class
        ).getResultList();
    }

    // ===============================
    // FIND BY PLANT
    // ===============================
    public List<GrowthForecast> findByPlantInstance(PlantInstance pianta) {
        return em.createQuery(
                        "SELECT gf FROM GrowthForecast gf WHERE gf.plantInstance = :pianta",
                        GrowthForecast.class
                ).setParameter("pianta", pianta)
                .getResultList();
    }

    // ===============================
    // FIND ORDINATI PER DATA
    // ===============================
    public List<GrowthForecast> findByPlantOrderByDate(PlantInstance pianta) {
        return em.createQuery(
                        "SELECT gf FROM GrowthForecast gf WHERE gf.plantInstance = :pianta ORDER BY gf.dateTime ASC",
                        GrowthForecast.class
                ).setParameter("pianta", pianta)
                .getResultList();
    }

    // ===============================
    // DELETE
    // ===============================
    public void deleteById(Integer id) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            GrowthForecast gf = em.find(GrowthForecast.class, id);
            if (gf != null) {
                em.remove(gf);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}