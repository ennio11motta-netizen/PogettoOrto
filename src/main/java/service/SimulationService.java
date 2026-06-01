

package service;

import dto.PlantSimulationResultDTO;
import dto.SimulationStepDTO;
import model.*;
import org.springframework.stereotype.Service;
import rdf.RdfService;
import repository.LocationRepository;
import repository.PlantInstanceRepository;
import simulation.SimulationProcessor;
import simulation.SimulationStepResult;
import util.IrrigationCalculator;
import util.IrrigationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Service orchestratore del sistema.
 *
 * Responsabilità:
 * - validare la simulazione generale
 * - recuperare dati meteo previsionali
 * - delegare l'elaborazione giornaliera a SimulationProcessor
 */
@Service
public class SimulationService {

    private final ExternalWeatherService externalWeatherService;
    private final RdfService rdfService;
    private final SimulationProcessor simulationProcessor;
    private final LocationRepository locationRepository;
    private final PlantInstanceRepository plantInstanceRepository;

    public SimulationService(
            ExternalWeatherService externalWeatherService,
            RdfService rdfService,
            SimulationProcessor simulationProcessor,
            LocationRepository locationRepository,
            PlantInstanceRepository plantInstanceRepository
    ) {
        if (externalWeatherService == null) {
            throw new IllegalArgumentException("ExternalWeatherService non può essere null");
        }

        if (rdfService == null) {
            throw new IllegalArgumentException("RdfService non può essere null");
        }

        if (simulationProcessor == null) {
            throw new IllegalArgumentException("SimulationProcessor non può essere null");
        }

        if (locationRepository == null) {
            throw new IllegalArgumentException("LocationRepository non può essere null");
        }

        if (plantInstanceRepository == null) {
            throw new IllegalArgumentException("PlantInstanceRepository non può essere null");
        }

        this.externalWeatherService = externalWeatherService;
        this.rdfService = rdfService;
        this.simulationProcessor = simulationProcessor;
        this.locationRepository = locationRepository;
        this.plantInstanceRepository = plantInstanceRepository;
    }

    // =========================================================
    // RUN SIMULATION - SINGOLA PIANTA
    // =========================================================
    public List<SimulationStepDTO> runSimulation(
            Integer locationId,
            Integer plantId,
            Integer giorni
    ) {
        validateRunSimulationRequest(locationId, plantId, giorni);

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location non trovata"));

        PlantInstance plant = plantInstanceRepository.findById(plantId)
                .orElseThrow(() -> new IllegalArgumentException("PlantInstance non trovata"));

        List<WeatherDay> forecast =
                externalWeatherService.fetchForecast(location, giorni);

        List<SimulationStepResult> results = new ArrayList<>();

        for (WeatherDay weatherDay : forecast) {
            results.add(simulationProcessor.processStep(plant, weatherDay));
        }

        finalizzaRDF();

        return toDTOList(results);
    }

    // =========================================================
    // RUN SIMULATION GIARDINO
    // =========================================================
    public List<PlantSimulationResultDTO> runGardenSimulation(
            Integer locationId,
            Integer giorni
    ) {
        validateRunGardenSimulationRequest(locationId, giorni);

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location non trovata"));

        List<PlantInstance> plants =
                plantInstanceRepository.findByLocation(location);

        if (plants.isEmpty()) {
            throw new IllegalArgumentException("Nessuna pianta trovata");
        }

        List<WeatherDay> forecast =
                externalWeatherService.fetchForecast(location, giorni);

        List<PlantSimulationResultDTO> response = new ArrayList<>();

        for (PlantInstance plant : plants) {
            List<SimulationStepResult> results = new ArrayList<>();

            for (WeatherDay weatherDay : forecast) {
                results.add(simulationProcessor.processStep(plant, weatherDay));
            }

            List<SimulationStepDTO> dtoResults = toDTOList(results);

            response.add(new PlantSimulationResultDTO(
                    plant.getPlantId(),
                    plant.getNome(),
                    location.getLocationId(),
                    location.getNome(),
                    dtoResults
            ));
        }

        finalizzaRDF();

        return response;
    }

    // =========================================================
    // VALIDAZIONI
    // =========================================================
    private void validateRunSimulationRequest(
            Integer locationId,
            Integer plantId,
            Integer giorni
    ) {
        if (locationId == null || plantId == null) {
            throw new IllegalArgumentException("Parametri obbligatori");
        }

        if (giorni == null || giorni <= 0 || giorni > 16) {
            throw new IllegalArgumentException("Giorni non validi");
        }
    }

    private void validateRunGardenSimulationRequest(
            Integer locationId,
            Integer giorni
    ) {
        if (locationId == null) {
            throw new IllegalArgumentException("Location obbligatoria");
        }

        if (giorni == null || giorni <= 0 || giorni > 16) {
            throw new IllegalArgumentException("Giorni non validi");
        }
    }

    // =========================================================
    // RDF
    // =========================================================
    public void finalizzaRDF() {
        rdfService.applicaInferenzaPiantePericolose();
        rdfService.salvaRDFSuFile("data/orto.ttl");
    }

    // =========================================================
    // MAPPER: SimulationStepResult -> SimulationStepDTO
    // =========================================================
    private List<SimulationStepDTO> toDTOList(List<SimulationStepResult> results) {
        List<SimulationStepDTO> dtoList = new ArrayList<>();

        for (SimulationStepResult result : results) {
            dtoList.add(toDTO(result));
        }

        return dtoList;
    }

    private SimulationStepDTO toDTO(SimulationStepResult result) {
        SimulationStepDTO dto = new SimulationStepDTO();

        WeatherDay weather = result.getWeatherDay();
        GrowthForecast forecast = result.getGrowthForecast();
        RiskAssessment risk = result.getRiskAssessment();

        if (weather != null) {
            dto.setWeatherId(weather.getWeatherId());

            if (weather.getData() != null) {
                dto.setDate(weather.getData().toString());
            }

            dto.setTempMin(weather.getTempMin());
            dto.setTempMax(weather.getTempMax());
            dto.setUmidita(weather.getUmidita());
            dto.setPrecipitazione(weather.getPrecipitazione());
            dto.setVentoKmh(weather.getVentoKmh());
            dto.setUvIndex(weather.getUvIndex());
        }

        if (forecast != null) {
            dto.setForecastId(forecast.getForecastId());
            dto.setPercentCiclo(forecast.getPercentCiclo());
            dto.setGddGiornaliero(forecast.getGddPrevistiGiorn());

            if (forecast.getStadioPrevisto() != null) {
                dto.setGrowthStage(forecast.getStadioPrevisto().name());
            }

            dto.setGiorniAllaMaturazione(forecast.getGiorniNuovoStadio());
        }

        if (risk != null) {
            dto.setRiskId(risk.getRiskId());

            if (risk.getPlantInstance() != null) {
                dto.setPlantId(risk.getPlantInstance().getPlantId());
                dto.setStoreGDD(risk.getPlantInstance().getStoreGDD());

                if (risk.getPlantInstance().getGrowthStage() != null) {
                    dto.setGrowthStage(risk.getPlantInstance().getGrowthStage().name());
                }
            }

            if (risk.getRiskCaldo() != null) {
                dto.setRiskCaldo(risk.getRiskCaldo().name());
            }

            if (risk.getRiskFreddo() != null) {
                dto.setRiskFreddo(risk.getRiskFreddo().name());
            }

            if (risk.getRiskVento() != null) {
                dto.setRiskVento(risk.getRiskVento().name());
            }

            if (risk.getRiskMalattia() != null) {
                dto.setRiskMalattia(risk.getRiskMalattia().name());
            }

            dto.setConsigli(risk.getConsigli());
        }

        // Calcolo irrigazione
        if (weather != null && risk != null && risk.getPlantInstance() != null) {
            IrrigationCalculator irrigationCalculator = new IrrigationCalculator();

            IrrigationResult irrigationResult =
                    irrigationCalculator.valutaIrrigazione(
                            risk.getPlantInstance(),
                            weather
                    );

            if (irrigationResult != null) {
                if (irrigationResult.getLevel() != null) {
                    dto.setIrrigationLevel(irrigationResult.getLevel().name());
                }

                dto.setIrrigationAdvice(irrigationResult.getAdvice());
            }
        }

        return dto;
    }
}
