package mainprova;

import jakarta.persistence.EntityManager;
import model.*;
import rdf.RdfQueryService;
import rdf.RdfService;
import simulation.SimulationService;
import util.JpaUtil;

import java.time.LocalDateTime;

public class Main1 {

    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();

        try {

            // ===============================
            // DATI REALI (SPECIE + LOCATION)
            // ===============================

            Location location = new Location();
            location.setNome("Milano");
            location.setLatitudine(45.46);
            location.setLongitudine(9.19);

            PlantSpecie specie = new PlantSpecie();
            specie.setNome("Lattuga");

            specie.setTempBase(5.0);
            specie.setGddEmergenza(30.0);
            specie.setGddSviluppo(120.0);
            specie.setGddFioritura(250.0);
            specie.setGddFruttificazione(350.0);
            specie.setGddMaturazione(500.0);

            specie.setSogliaStressCaldo(28.0);
            specie.setSogliaStressFreddo(2.0);
            specie.setSensibilitaMalattie("media");

            PlantInstance pianta = new PlantInstance();
            pianta.setPlantSpecie(specie);
            pianta.setStoreGDD(180.0);
            pianta.setDataInsert(LocalDateTime.now());

            em.getTransaction().begin();
            em.persist(location);
            em.persist(specie);
            em.persist(pianta);
            em.getTransaction().commit();

            // ===============================
            // SIMULAZIONE CON API REALI
            // ===============================

            SimulationService sim = new SimulationService(em);

            // 🔥 aumentiamo giorni per avere più variabilità reale
            sim.simula(location, pianta, 7);

            // ===============================
            // RDF
            // ===============================

            RdfService rdfService = sim.getRdfService();

            // ===============================
            // INFERENZA
            // ===============================

            rdfService.applicaInferenzaPiantePericolose();

            // ===============================
            // OUTPUT RDF
            // ===============================

            System.out.println("\n===== RDF GENERATO =====");
            rdfService.printRDF();

            // ===============================
            // QUERY
            // ===============================

            RdfQueryService queryService =
                    new RdfQueryService(rdfService.getModel());

            System.out.println("\n===== QUERY =====");

            System.out.println("\n--- Tutti i rischi ---");
            queryService.queryTuttiIRischi();

            System.out.println("\n--- Piante con rischio ---");
            queryService.queryPianteConRischio();

            System.out.println("\n--- Piante avanzate ---");
            queryService.queryPianteAvanzateConRischio();

            System.out.println("\n--- Piante in pericolo ---");
            queryService.queryPianteInPericolo();
//
//            // ===============================
//            // EXPORT RDF
//            // ===============================
//
//            rdfService.salvaRDFSuFile("grafo.ttl");
//
//            System.out.println("\n✅ File RDF salvato");

        } finally {
            em.close();
            JpaUtil.close();
        }
    }
}