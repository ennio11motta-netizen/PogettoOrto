package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import model.Location;
import model.WeatherDay;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class WeatherDayRepository {

    private final EntityManager em;

    public WeatherDayRepository(EntityManager em) {
        this.em = em;
    }

    // ===============================
    // SAVE
    // ===============================
    public WeatherDay save(WeatherDay wd) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(wd);
            tx.commit();
            return wd;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    // ===============================
    // FIND BY ID
    // ===============================
    public Optional<WeatherDay> findById(Integer id) {
        return Optional.ofNullable(em.find(WeatherDay.class, id));
    }

    // ===============================
    // FIND ALL
    // ===============================
    public List<WeatherDay> findAll() {
        return em.createQuery(
                "SELECT wd FROM WeatherDay wd",
                WeatherDay.class
        ).getResultList();
    }

    // ===============================
    // FIND PER LOCATION
    // ===============================
    public List<WeatherDay> findByLocation(Location location) {
        return em.createQuery(
                        "SELECT wd FROM WeatherDay wd WHERE wd.location = :location",
                        WeatherDay.class
                ).setParameter("location", location)
                .getResultList();
    }

    // ===============================
    // FIND PER LOCATION + DATA SINGOLA
    // ===============================
    public Optional<WeatherDay> findByLocationAndDate(Location location, LocalDate data) {
        try {
            return Optional.of(
                    em.createQuery(
                                    "SELECT wd FROM WeatherDay wd WHERE wd.location = :location AND wd.data = :data",
                                    WeatherDay.class
                            ).setParameter("location", location)
                            .setParameter("data", data)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // ===============================
    // FIND PER LOCATION ORDINATO
    // ===============================
    public List<WeatherDay> findByLocationOrderByDateAsc(Location location) {
        return em.createQuery(
                        "SELECT wd FROM WeatherDay wd WHERE wd.location = :location ORDER BY wd.data ASC",
                        WeatherDay.class
                ).setParameter("location", location)
                .getResultList();
    }

    // ===============================
    // FIND INTERVALLO DATE
    // ===============================
    public List<WeatherDay> findByLocationAndDateBetween(
            Location location,
            LocalDate startDate,
            LocalDate endDate) {

        return em.createQuery(
                        "SELECT wd FROM WeatherDay wd WHERE wd.location = :location AND wd.data BETWEEN :start AND :end ORDER BY wd.data ASC",
                        WeatherDay.class
                ).setParameter("location", location)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .getResultList();
    }

    // ===============================
    // UPDATE
    // ===============================
    public WeatherDay update(WeatherDay wd) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            WeatherDay updated = em.merge(wd);
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
            WeatherDay wd = em.find(WeatherDay.class, id);
            if (wd != null) em.remove(wd);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    public Optional<WeatherDay> findByLocationAndDay(
            Location location,
            LocalDateTime startOfDay,
            LocalDateTime startOfNextDay
    ) {
        try {
            WeatherDay result = em.createQuery(
                            "SELECT wd FROM WeatherDay wd " +
                                    "WHERE wd.location = :location " +
                                    "AND wd.data >= :startOfDay " +
                                    "AND wd.data < :startOfNextDay",
                            WeatherDay.class
                    )
                    .setParameter("location", location)
                    .setParameter("startOfDay", startOfDay)
                    .setParameter("startOfNextDay", startOfNextDay)
                    .setMaxResults(1)
                    .getSingleResult();

            return Optional.of(result);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}