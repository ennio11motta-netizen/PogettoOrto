package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import model.PlantInstance;
import model.RiskAssessment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RiskAssessmentRepository {

    private final EntityManager em;

    public RiskAssessmentRepository(EntityManager em) {
        this.em = em;
    }

    public RiskAssessment save(RiskAssessment risk) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(risk);
            tx.commit();
            return risk;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public RiskAssessment update(RiskAssessment risk) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            RiskAssessment updated = em.merge(risk);
            tx.commit();
            return updated;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public Optional<RiskAssessment> findById(Integer id) {
        return Optional.ofNullable(
                em.find(RiskAssessment.class, id)
        );
    }

    public List<RiskAssessment> findAll() {
        return em.createQuery(
                "SELECT ra FROM RiskAssessment ra",
                RiskAssessment.class
        ).getResultList();
    }

    public List<RiskAssessment> findByPlantInstance(PlantInstance pianta) {
        return em.createQuery(
                        "SELECT ra FROM RiskAssessment ra WHERE ra.plantInstance = :pianta",
                        RiskAssessment.class
                )
                .setParameter("pianta", pianta)
                .getResultList();
    }

    public List<RiskAssessment> findByPlantInstanceOrderByDateAsc(PlantInstance pianta) {
        return em.createQuery(
                        "SELECT ra FROM RiskAssessment ra " +
                                "WHERE ra.plantInstance = :pianta " +
                                "ORDER BY ra.dateTime ASC",
                        RiskAssessment.class
                )
                .setParameter("pianta", pianta)
                .getResultList();
    }

    public Optional<RiskAssessment> findByPlantInstanceAndDate(
            PlantInstance pianta,
            LocalDateTime data
    ) {
        try {
            RiskAssessment result = em.createQuery(
                            "SELECT ra FROM RiskAssessment ra " +
                                    "WHERE ra.plantInstance = :pianta " +
                                    "AND ra.dateTime = :data",
                            RiskAssessment.class
                    )
                    .setParameter("pianta", pianta)
                    .setParameter("data", data)
                    .getSingleResult();

            return Optional.of(result);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public RiskAssessment saveOrUpdateByPlantAndDate(RiskAssessment assessment) {
        validateAssessmentForSaveOrUpdate(assessment);

        Optional<RiskAssessment> existing =
                findByPlantInstanceAndDate(
                        assessment.getPlantInstance(),
                        assessment.getDateTime()
                );

        if (existing.isPresent()) {
            RiskAssessment old = existing.get();

            old.setRiskCaldo(assessment.getRiskCaldo());
            old.setRiskFreddo(assessment.getRiskFreddo());
            old.setRiskVento(assessment.getRiskVento());
            old.setRiskMalattia(assessment.getRiskMalattia());
            old.setConsigli(assessment.getConsigli());

            return update(old);
        }

        return save(assessment);
    }

    public void deleteById(Integer id) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            RiskAssessment risk = em.find(RiskAssessment.class, id);

            if (risk != null) {
                em.remove(risk);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    private void validateAssessmentForSaveOrUpdate(RiskAssessment assessment) {
        if (assessment == null) {
            throw new IllegalArgumentException("RiskAssessment non può essere null");
        }

        if (assessment.getPlantInstance() == null) {
            throw new IllegalArgumentException("RiskAssessment deve avere una PlantInstance");
        }

        if (assessment.getDateTime() == null) {
            throw new IllegalArgumentException("RiskAssessment deve avere una data");
        }
    }
}
