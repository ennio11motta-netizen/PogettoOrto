package repository;
import model.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LocationRepository {

    private final EntityManager em;

    public LocationRepository(EntityManager em) {
        this.em = em;
    }

    // ===============================
    // SAVE
    // ===============================
    public Location save(Location location) {
        em.persist(location);
        return  location;
    }

    // ===============================
    // FIND BY ID
    // ===============================
    public Optional<Location> findById(Integer id) {
        return Optional.ofNullable(em.find(Location.class, id));
    }

    // ===============================
    // FIND ALL
    // ===============================
    public List<Location> findAll() {
        return em.createQuery(
                "SELECT l FROM Location l",
                Location.class
        ).getResultList();
    }

    // ===============================
    // FIND BY NOME
    // ===============================
    public Optional<Location> findByNome(String nome) {
        try {
            return Optional.of(
                    em.createQuery(
                                    "SELECT l FROM Location l WHERE l.nome = :nome",
                                    Location.class
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
    public Location update(Location location) {
        return em.merge(location);

    }

    // ===============================
    // DELETE
    // ===============================
    public void deleteById(Integer id) {

        Location location = em.find(Location.class, id);

        if (location != null) {
            em.remove(location);
        }
    }


}
