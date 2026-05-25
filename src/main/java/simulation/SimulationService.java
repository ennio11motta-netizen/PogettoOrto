package simulation;

import jakarta.persistence.EntityManager;
import model.Location;
import model.PlantInstance;
import model.WeatherDay;
import rdf.RdfService;
import service.ExternalWeatherService;
import service.PlantService;
import service.RiskService;
import service.WeatherService;
import simulation.SimulationProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Service orchestratore del sistema.
 *
 * Responsabilità:
 * - validare la simulazione generale
 * - recuperare dati meteo previsionali
 * - delegare l'elaborazione giornaliera a SimulationProcessor
 *
 * Non contiene più direttamente la logica dettagliata di:
 * - salvataggio meteo
 * - aggiornamento crescita
 * - calcolo rischio
 * - export RDF
 */
public class SimulationService {

    private final ExternalWeatherService externalWeatherService;
    private final WeatherService weatherService;
    private final PlantService plantService;
    private final RiskService riskService;
    private final RdfService rdfService;
    private final SimulationProcessor simulationProcessor;

    public SimulationService(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager non può essere null");
        }

        this.externalWeatherService = new ExternalWeatherService();
        this.weatherService = new WeatherService(em);
        this.plantService = new PlantService(em);
        this.riskService = new RiskService(em);
        this.rdfService = new RdfService();

        this.simulationProcessor = new SimulationProcessor(
                weatherService,
                plantService,
                riskService,
                rdfService
        );
    }

    public List<SimulationStepResult> simula(
            Location location,
            PlantInstance pianta,
            Integer giorni
    ) {
        validateInput(location, pianta, giorni);

        /*
         * 1. Recupero dati meteo previsionali.
         */
        List<WeatherDay> forecast =
                externalWeatherService.fetchForecast(location, giorni);

        /*
         * 2. Elaborazione giornaliera delegata al processor.
         */
        List<SimulationStepResult> results = new ArrayList<>();

        for (WeatherDay weatherDay : forecast) {
            SimulationStepResult result =
                    simulationProcessor.processStep(pianta, weatherDay);

            results.add(result);
        }

        return results;
    }

    public void stampaRDF() {
        rdfService.printRDF();
    }

    public RdfService getRdfService() {
        return rdfService;
    }

    private void validateInput(
            Location location,
            PlantInstance pianta,
            Integer giorni
    ) {
        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (giorni == null || giorni <= 0) {
            throw new IllegalArgumentException("Numero giorni non valido");
        }
    }
}