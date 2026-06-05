
package controller;

import dto.PlantSpecieDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reqResp.CreatePlantSpecieRequest;
import service.PlantSpecieService;

import java.util.List;

@RestController
@RequestMapping("/api/species")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantSpecieController {

    private final PlantSpecieService plantSpecieService;

    public PlantSpecieController(PlantSpecieService plantSpecieService) {
        this.plantSpecieService = plantSpecieService;
    }

    @GetMapping
    public List<PlantSpecieDTO> getAllSpecies() {
        return plantSpecieService.getAllSpecies();
    }

    @GetMapping("/{id}")
    public PlantSpecieDTO getSpecieById(@PathVariable Integer id) {
        return plantSpecieService.getSpecieById(id);
    }

    @PostMapping
    public PlantSpecieDTO createSpecie(@RequestBody CreatePlantSpecieRequest request) {
        return plantSpecieService.createSpecie(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecie(@PathVariable Integer id) {
        plantSpecieService.deleteSpecie(id);
        return ResponseEntity.noContent().build();
    }
}
