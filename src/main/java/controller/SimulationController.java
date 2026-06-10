package controller;

import dto_mapper.GardenSimulationRequest;
import dto_mapper.PlantSimulationResultDTO;
import dto_mapper.SimulationStepDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import model.GrowthForecast;
import model.Location;
import model.PlantInstance;
import model.RiskAssessment;
import model.WeatherDay;
import org.springframework.web.bind.annotation.*;
import repository.PlantInstanceRepository;
import simulation.SimulationService;
import simulation.SimulationStepResult;
import util.IrrigationCalculator;
import util.IrrigationResult;

import java.util.ArrayList;
import java.util.List;




@RestController
@RequestMapping("/api/simulation")
@CrossOrigin(origins = "http://localhost:5173")

public class SimulationController {

    private final EntityManagerFactory entityManagerFactory;

    public SimulationController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @PostMapping("/run")
    public List<SimulationStepDTO> runSimulation(@RequestBody SimulationRunRequest request) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Location location = em.find(Location.class, request.getLocationId());
            PlantInstance plant = em.find(PlantInstance.class, request.getPlantId());

            if (location == null) {
                throw new IllegalArgumentException(
                        "Location non trovata con id: " + request.getLocationId()
                );
            }

            if (plant == null) {
                throw new IllegalArgumentException(
                        "PlantInstance non trovata con id: " + request.getPlantId()
                );
            }

            SimulationService simulationService = new SimulationService(em);

            List<SimulationStepResult> results =
                    simulationService.simula(location, plant, request.getGiorni());

            return toDTOList(results);

        } finally {
            em.close();
        }
    }

    private List<SimulationStepDTO> toDTOList(List<SimulationStepResult> results) {
        List<SimulationStepDTO> dtoList = new ArrayList<>();

        for (SimulationStepResult result : results) {
            dtoList.add(toDTO(result));
        }

        return dtoList;
    }

//
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

            /*
             * Nel PlantService il campo gddPrevistiGiorn viene valorizzato
             * con il GDD giornaliero calcolato.
             */
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

        /*
         * Calcolo irrigazione.
         * Lo facciamo dopo aver verificato che weather, risk e plantInstance esistano.
         */
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









    @PostMapping("/run-garden")
    public List<PlantSimulationResultDTO> runGardenSimulation(
            @RequestBody GardenSimulationRequest request
    ) {
        validateGardenSimulationRequest(request);

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Location location = em.find(Location.class, request.getLocationId());

            if (location == null) {
                throw new IllegalArgumentException(
                        "Location non trovata con id: " + request.getLocationId()
                );
            }

            PlantInstanceRepository plantRepository =
                    new PlantInstanceRepository(em);

            List<PlantInstance> plants =
                    plantRepository.findByLocation(location);

            if (plants.isEmpty()) {
                throw new IllegalArgumentException(
                        "Nessuna pianta trovata per locationId: " + request.getLocationId()
                );
            }

            SimulationService simulationService = new SimulationService(em);

            List<PlantSimulationResultDTO> response = new ArrayList<>();

            for (PlantInstance plant : plants) {
                List<SimulationStepResult> results =
                        simulationService.simula(location, plant, request.getGiorni());

                List<SimulationStepDTO> dtoResults = toDTOList(results);

                response.add(new PlantSimulationResultDTO(
                        plant.getPlantId(),
                        plant.getNome(),
                        location.getLocationId(),
                        location.getNome(),
                        dtoResults
                ));
            }

            return response;

        } finally {
            em.close();
        }
    }


    private void validateGardenSimulationRequest(GardenSimulationRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        if (request.getLocationId() == null) {
            throw new IllegalArgumentException("La location è obbligatoria");
        }

        if (request.getGiorni() == null || request.getGiorni() <= 0) {
            throw new IllegalArgumentException("Numero giorni non valido");
        }

        if (request.getGiorni() > 16) {
            throw new IllegalArgumentException("Numero giorni non valido: massimo 16");
        }
    }
}