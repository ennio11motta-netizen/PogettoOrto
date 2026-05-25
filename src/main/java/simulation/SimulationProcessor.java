package simulation;


import model.GrowthForecast;
import model.PlantInstance;
import model.RiskAssessment;
import model.WeatherDay;
import rdf.RdfService;
import service.PlantService;
import service.RiskService;
import service.WeatherService;

public class SimulationProcessor {

    private final WeatherService weatherService;
    private final PlantService plantService;
    private final RiskService riskService;
    private final RdfService rdfService;

    public SimulationProcessor(
            WeatherService weatherService,
            PlantService plantService,
            RiskService riskService,
            RdfService rdfService
    ) {
        if (weatherService == null) {
            throw new IllegalArgumentException("WeatherService non può essere null");
        }

        if (plantService == null) {
            throw new IllegalArgumentException("PlantService non può essere null");
        }

        if (riskService == null) {
            throw new IllegalArgumentException("RiskService non può essere null");
        }

        if (rdfService == null) {
            throw new IllegalArgumentException("RdfService non può essere null");
        }

        this.weatherService = weatherService;
        this.plantService = plantService;
        this.riskService = riskService;
        this.rdfService = rdfService;
    }

    public SimulationStepResult processStep(
            PlantInstance pianta,
            WeatherDay weatherDay
    ) {
        validateInput(pianta, weatherDay);

        /*
         * 1. Salvataggio dato meteo.
         */
        WeatherDay savedWeatherDay =
                weatherService.salvaWeatherDay(weatherDay);

        /*
         * 2. Aggiornamento crescita e generazione forecast.
         */
        GrowthForecast growthForecast =
                plantService.aggiornaCrescitaEGeneraForecast(
                        pianta,
                        savedWeatherDay,
                        null
                );

        /*
         * 3. Calcolo e salvataggio rischio.
         */
        RiskAssessment riskAssessment =
                riskService.generaESalvaValutazione(
                        pianta,
                        savedWeatherDay
                );

        /*
         * 4. Esportazione RDF delle entità.
         */
        rdfService.exportWeatherDay(savedWeatherDay);
        rdfService.exportPlantInstance(pianta);
        rdfService.exportGrowthForecast(growthForecast);
        rdfService.exportRiskAssessment(riskAssessment);

        /*
         * 5. Creazione relazioni RDF.
         */
        rdfService.collegaRelazioni(pianta, savedWeatherDay, riskAssessment);
        rdfService.collegaPlantForecast(pianta, growthForecast);
        rdfService.collegaPlantRisk(pianta, riskAssessment);

        return new SimulationStepResult(
                savedWeatherDay,
                growthForecast,
                riskAssessment
        );
    }

    private void validateInput(
            PlantInstance pianta,
            WeatherDay weatherDay
    ) {
        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (weatherDay == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }
    }
}