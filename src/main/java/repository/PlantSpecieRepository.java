//package repository;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import jakarta.persistence.NoResultException;
//import model.PlantSpecie;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public class PlantSpecieRepository {
//
//    private final EntityManager em;
//
//    public PlantSpecieRepository(EntityManager em) {
//        this.em = em;
//    }
//
//    // ===============================
//    // SAVE
//    // ===============================
//    @Transactional
//    public PlantSpecie save(PlantSpecie specie) {
//
//        em.persist(specie);
//        return specie;
//
//
//    }
//
//    // ===============================
//    // FIND BY ID
//    // ===============================
//    public Optional<PlantSpecie> findById(Integer id) {
//        return Optional.ofNullable(em.find(PlantSpecie.class, id));
//    }
//
//    // ===============================
//    // FIND ALL
//    // ===============================
//    public List<PlantSpecie> findAll() {
//        return em.createQuery(
//                "SELECT ps FROM PlantSpecie ps",
//                PlantSpecie.class
//        ).getResultList();
//    }
//
//    // ===============================
//    // FIND BY NOME
//    // ===============================
//    public Optional<PlantSpecie> findByNome(String nome) {
//        try {
//            return Optional.of(
//                    em.createQuery(
//                                    "SELECT ps FROM PlantSpecie ps WHERE ps.nome = :nome",
//                                    PlantSpecie.class
//                            ).setParameter("nome", nome)
//                            .getSingleResult()
//            );
//        } catch (NoResultException e) {
//            return Optional.empty();
//        }
//    }
//
//    // ===============================
//    // UPDATE
//    // ===============================
//    public PlantSpecie update(PlantSpecie specie) {
//        return em.merge(specie);
//
//    }
//
//    // ===============================
//    // DELETE
//    // ===============================
//    public void deleteById(Integer id) {
//
//        PlantSpecie specie = em.find(PlantSpecie.class, id);
//
//        if (specie != null) {
//            em.remove(specie);
//        }
//
//    }
//}
package repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.PlantSpecie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlantSpecieRepository {

    private final EntityManager em;

    public PlantSpecieRepository(EntityManager em) {
        this.em = em;
    }

    // ===============================
    // SAVE
    // ===============================
    public PlantSpecie save(PlantSpecie specie) {
        em.persist(specie);
        return specie;
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
                            )
                            .setParameter("nome", nome)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // ===============================
    // FIND BY NOME IGNORE CASE
    // ===============================
    public Optional<PlantSpecie> findByNomeIgnoreCase(String nome) {
        return em.createQuery(
                        "SELECT ps FROM PlantSpecie ps WHERE LOWER(ps.nome) = LOWER(:nome)",
                        PlantSpecie.class
                )
                .setParameter("nome", nome)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }

    // ===============================
    // UPDATE
    // ===============================
    public PlantSpecie update(PlantSpecie specie) {
        return em.merge(specie);
    }

    // ===============================
    // DELETE
    // ===============================
    public void deleteById(Integer id) {
        PlantSpecie specie = em.find(PlantSpecie.class, id);

        if (specie != null) {
            em.remove(specie);
        }
    }
}
