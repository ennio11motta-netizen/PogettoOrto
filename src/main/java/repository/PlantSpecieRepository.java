package repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import model.PlantSpecie;
import java.util.List;
import java.util.Optional;

public class PlantSpecieRepository {

    private final EntityManager em;

    public PlantSpecieRepository(EntityManager em) {
        this.em = em;
    }

    // ===============================
    // SAVE
    // ===============================
    public PlantSpecie save(PlantSpecie specie) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(specie);
            tx.commit();
            return specie;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // ===============================
    // FIND BY ID
    // ===============================
    public Optional<PlantSpecie> findById(Integer id) {
        return Optional.ofNullable(em.find(PlantSpecie.class, id));
    }

    // ===============================
    // FIND ALL
    // ===============================
    public List<PlantSpecie> findAll() {
        return em.createQuery(
                "SELECT ps FROM PlantSpecie ps",
                PlantSpecie.class
        ).getResultList();
    }

    // ===============================
    // FIND BY NOME
    // ===============================
    public Optional<PlantSpecie> findByNome(String nome) {
        try {
            return Optional.of(
                    em.createQuery(
                                    "SELECT ps FROM PlantSpecie ps WHERE ps.nome = :nome",
                                    PlantSpecie.class
                            ).setParameter("nome", nome)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // ===============================
    // UPDATE
    // ===============================
    public PlantSpecie update(PlantSpecie specie) {
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            PlantSpecie updated = em.merge(specie);
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
            PlantSpecie specie = em.find(PlantSpecie.class, id);
            if (specie != null) em.remove(specie);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
