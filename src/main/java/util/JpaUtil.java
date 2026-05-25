package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility per la gestione di JPA.
 * Si occupa di:
 * - creare l'EntityManagerFactory (singleton)
 * - fornire EntityManager
 * - chiudere le risorse
 */
public class JpaUtil {

    private static final String PERSISTENCE_UNIT_NAME = "DefaultPersistenceUnit";

    // factory condivisa (singleton)
    private static EntityManagerFactory entityManagerFactory;

    private JpaUtil() {
        // evita istanziazione (classe utility)
    }

    /**
     * Restituisce l'EntityManagerFactory (singleton).
     */
    public static EntityManagerFactory getEntityManagerFactory() {

        if (entityManagerFactory == null) {
            try {
                entityManagerFactory =
                        Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

            } catch (Exception e) {

                System.err.println("❌ Errore creazione EntityManagerFactory");
                e.printStackTrace();

                throw new RuntimeException("Errore inizializzazione JPA", e);
            }
        }

        return entityManagerFactory;
    }

    /**
     * Crea un nuovo EntityManager.
     * Ogni chiamata restituisce un'istanza nuova.
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Chiude l'EntityManagerFactory (da chiamare alla fine dell'applicazione).
     */
    public static void close() {

        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
