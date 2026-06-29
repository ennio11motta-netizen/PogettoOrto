
package simulation;

import exception.SimulationMode;
import model.data.GrowthForecast;
import model.data.PlantInstance;
import model.data.RiskAssessment;
import model.data.WeatherDay;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.rdf.RdfService;
import service.model.PlantService;
import service.model.RiskService;
import service.model.WeatherService;

@Service
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

    //////////////////////////////////
    @Transactional
    public SimulationStepResult processStep(
            PlantInstance pianta,
            WeatherDay weatherDay,
            SimulationMode mode

    ) {
        validateInput(pianta, weatherDay);



        if (mode == null) {
            throw new IllegalArgumentException("SimulationMode non può essere null");
        }


        if (mode == SimulationMode.PREVIEW) {
            return processPreviewStep(pianta, weatherDay);
        }

        if (mode == SimulationMode.APPLIED) {
            return processAppliedStep(pianta, weatherDay);
        }


        throw new IllegalArgumentException("SimulationMode non gestito: " + mode);
    }

//
//    /*
//         * 1. Salvataggio dato meteo.
//         */
//        WeatherDay savedWeatherDay =
//                weatherService.salvaWeatherDay(weatherDay);
//
//        /*
//         * 2. Aggiornamento crescita e generazione forecast.
//         */
//        GrowthForecast growthForecast =
//                plantService.aggiornaCrescitaEGeneraForecast(
//                        pianta,
//                        savedWeatherDay,
//                        null
//                );
//
//        /*
//         * 3. Calcolo e salvataggio rischio.
//         */
//        RiskAssessment riskAssessment =
//                riskService.generaESalvaValutazione(
//                        pianta,
//                        savedWeatherDay
//                );
//
//
//        if (mode == SimulationMode.APPLIED) {
//            /*
//             * 4. Esportazione RDF delle entità.
//             */
//            rdfService.exportWeatherDay(savedWeatherDay);
//            rdfService.exportPlantInstance(pianta);
//            rdfService.exportGrowthForecast(growthForecast);
//            rdfService.exportRiskAssessment(riskAssessment);
//
//            /*
//             * 5. Creazione relazioni RDF.
//             */
//            rdfService.collegaRelazioni(pianta, savedWeatherDay, riskAssessment);
//            rdfService.collegaPlantForecast(pianta, growthForecast);
//            rdfService.collegaForecastWeather(growthForecast, savedWeatherDay);
//            rdfService.collegaPlantRisk(pianta, riskAssessment);
//        }
//        return new SimulationStepResult(
//                savedWeatherDay,
//                growthForecast,
//                riskAssessment
//        );
//    }


    @Transactional
    public SimulationStepResult processStep(
            String simulationUri,
            PlantInstance pianta,
            WeatherDay weatherDay,
            SimulationMode mode
    ) {
        SimulationStepResult result = processStep(pianta, weatherDay, mode);


        if (mode == SimulationMode.APPLIED) {

            rdfService.collegaSimulationStep(
                    simulationUri,
                    pianta,
                    result.getWeatherDay(),
                    result.getRiskAssessment(),
                    result.getGrowthForecast()
            );
        }
        return result;
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



    private SimulationStepResult processPreviewStep(
            PlantInstance pianta,
            WeatherDay weatherDay
    ) {
        /*
         * PREVIEW:
         * - non salva WeatherDay
         * - non aggiorna PlantInstance reale
         * - non salva GrowthForecast
         * - non salva RiskAssessment
         * - non esporta RDF
         */

        GrowthForecast growthForecast =
                plantService.calcolaForecastPreview(
                        pianta,
                        weatherDay,
                        null
                );

        /*
         * Usiamo la copia preview della pianta, perché contiene
         * storeGDD e growthStage previsti.
         */
        PlantInstance previewPlant = growthForecast.getPlantInstance();

        RiskAssessment riskAssessment =
                riskService.generaValutazionePreview(
                        previewPlant,
                        weatherDay
                );

        return new SimulationStepResult(
                weatherDay,
                growthForecast,
                riskAssessment
        );
    }


    private SimulationStepResult processAppliedStep(
            PlantInstance pianta,
            WeatherDay weatherDay
    ) {
        /*
         * APPLIED:
         * - salva WeatherDay
         * - aggiorna PlantInstance reale
         * - salva GrowthForecast
         * - salva RiskAssessment
         * - esporta RDF
         */

        WeatherDay savedWeatherDay =
                weatherService.salvaWeatherDay(weatherDay);

        GrowthForecast growthForecast =
                plantService.aggiornaCrescitaEGeneraForecast(
                        pianta,
                        savedWeatherDay,
                        null
                );

        RiskAssessment riskAssessment =
                riskService.generaESalvaValutazione(
                        pianta,
                        savedWeatherDay
                );

        rdfService.exportWeatherDay(savedWeatherDay);
        rdfService.exportPlantInstance(pianta);
        rdfService.exportGrowthForecast(growthForecast);
        rdfService.exportRiskAssessment(riskAssessment);

        rdfService.collegaRelazioni(
                pianta,
                savedWeatherDay,
                riskAssessment
        );

        rdfService.collegaPlantForecast(
                pianta,
                growthForecast
        );

        rdfService.collegaForecastWeather(
                growthForecast,
                savedWeatherDay
        );

        rdfService.collegaPlantRisk(
                pianta,
                riskAssessment
        );

        return new SimulationStepResult(
                savedWeatherDay,
                growthForecast,
                riskAssessment
        );
    }
}