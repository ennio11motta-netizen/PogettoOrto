package mainprova;

import jakarta.persistence.EntityManager;
import model.*;
import rdf.RdfQueryService;
import rdf.RdfService;
import service.*;
import simulation.SimulationService;
import util.JpaUtil;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        EntityManager em = JpaUtil.getEntityManager();

        try {

            // ===============================
            // CREAZIONE DATI BASE
            // ===============================

            Location location = new Location();
            location.setNome("Perugia");
            location.setLatitudine(43.11);
            location.setLongitudine(12.39);

            PlantSpecie specie = new PlantSpecie();
            specie.setNome("Pomodoro");

            specie.setTempBase(10.0);
            specie.setGddEmergenza(50.0);
            specie.setGddSviluppo(200.0);
            specie.setGddFioritura(400.0);
            specie.setGddFruttificazione(600.0);
            specie.setGddMaturazione(800.0);

            specie.setSogliaStressCaldo(35.0);
            specie.setSogliaStressFreddo(5.0);
            specie.setSensibilitaMalattie("alta");

            PlantInstance pianta = new PlantInstance();
            pianta.setPlantSpecie(specie);
            pianta.setStoreGDD(650.0);
            pianta.setDataInsert(LocalDateTime.now());

            em.getTransaction().begin();
            em.persist(location);
            em.persist(specie);
            em.persist(pianta);
            em.getTransaction().commit();

            // ===============================
            // SIMULAZIONE BASE (3 giorni)
            // ===============================
            SimulationService sim = new SimulationService(em);
            sim.simula(location, pianta, 3);

            // ===============================
            // NUOVO RECORD (SCENARIO CRITICO)
            // ===============================

            WeatherDay wdCritico = new WeatherDay();

            wdCritico.setData(LocalDateTime.of(2026, 6, 1, 0, 0));
            wdCritico.setLocation(location);

            // valori completamente diversi (giornata critica)
            wdCritico.setTempMax(38.0);
            wdCritico.setTempMin(24.0);
            wdCritico.setUmidita(85.0);
            wdCritico.setPrecipitazione(7.0);
            wdCritico.setUvIndex(9.5);
            wdCritico.setVentoKmh(5.0);

            em.getTransaction().begin();
            em.persist(wdCritico);
            em.getTransaction().commit();

            // genera rischio su questo giorno extra
            RiskService riskService = new RiskService(em);
            RiskAssessment riskCritico = riskService.generaESalvaValutazione(pianta, wdCritico);

            // ===============================
            // RDF
            // ===============================
            RdfService rdfService = sim.getRdfService();

            rdfService.exportWeatherDay(wdCritico);
            rdfService.exportRiskAssessment(riskCritico);
            rdfService.exportPlantInstance(pianta);
            rdfService.collegaRelazioni(pianta, wdCritico, riskCritico);
            rdfService.collegaPlantRisk(pianta, riskCritico);

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
            RdfQueryService queryService = new RdfQueryService(rdfService.getModel());

            System.out.println("\n===== QUERY =====");

            queryService.queryTuttiIRischi();
            queryService.queryPianteConRischio();
            queryService.queryPianteAvanzateConRischio();
            queryService.queryPianteInPericolo();

            // ===============================
            // EXPORT
            // ===============================
            rdfService.salvaRDFSuFile("grafo.ttl");

            System.out.println("\n✅ File RDF salvato: grafo.ttl");

        } finally {
            em.close();
            JpaUtil.close();
        }
    }
}