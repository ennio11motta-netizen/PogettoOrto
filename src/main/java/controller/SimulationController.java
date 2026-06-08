package controller;

import org.springframework.http.ResponseEntity;
import reqResp.GardenSimulationRequest;
import dto.PlantSimulationResultDTO;
import dto.SimulationStepDTO;
import org.springframework.web.bind.annotation.*;
import reqResp.SimulationRunRequest;
import service.SimulationService;
import java.util.List;

@RestController
@RequestMapping("/api/simulation")
@CrossOrigin(origins = "http://localhost:5173")

public class SimulationController {

    private final SimulationService simulationService;


    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }



    @PostMapping("/run")
    public List<SimulationStepDTO> runSimulation(@RequestBody SimulationRunRequest request) {


               return simulationService.runSimulation(
                        request.getLocationId(),
                        request.getPlantId(),
                        request.getGiorni()
                );

    }



    @PostMapping("/run-garden")
    public List<PlantSimulationResultDTO> runGardenSimulation(
            @RequestBody GardenSimulationRequest request
    ) {

        validateGardenSimulationRequest(request);

        return simulationService.runGardenSimulation(
                request.getLocationId(),
                request.getGiorni()
        );
    }

    @DeleteMapping("/garden/{locationId}")
    public ResponseEntity<Void> deleteGardenSimulation(@PathVariable Integer locationId) {
        simulationService.deleteGardenSimulation(locationId);
        return ResponseEntity.noContent().build();
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