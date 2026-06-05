package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.GrowthForecast;
import model.PlantInstance;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class GrowthForecastRepository {

    private final EntityManager em;

    public GrowthForecastRepository(EntityManager em) {
        this.em = em;
    }

    // ===============================
    // SAVE
    // ===============================
    public GrowthForecast save(GrowthForecast gf) {
        em.persist(gf);
        return gf;

    }

    // ===============================
    // UPDATE
    // ===============================
    public GrowthForecast update(GrowthForecast gf) {
        return  em.merge(gf);

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


        GrowthForecast gf = em.find(GrowthForecast.class, id);

        if (gf != null) {
            em.remove(gf);
        }

    }

//    public void deleteByPlantInstance(PlantInstance pianta) {
//        List<GrowthForecast> forecasts = findByPlantInstance(pianta);
//
//        for (GrowthForecast forecast : forecasts) {
//            em.remove(forecast);
//        }
//    }



    public int deleteByPlantInstance(PlantInstance pianta) {
        return em.createQuery(
                        "DELETE FROM GrowthForecast gf WHERE gf.plantInstance = :pianta"
                )
                .setParameter("pianta", pianta)
                .executeUpdate();
    }

}