package controller;



import org.springframework.http.ResponseEntity;
import reqResp.CreatePlantRequest;
import dto.PlantDTO;
import org.springframework.web.bind.annotation.*;
import service.PlantService;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantController {


    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }


    @PostMapping
    public PlantDTO createPlant(@RequestBody CreatePlantRequest request) {
        return plantService.createPlant(request);
    }

    @GetMapping
    public List<PlantDTO> getPlantsByLocation(@RequestParam Integer locationId) {
        return plantService.getPlantsByLocation(locationId);
    }

    @GetMapping("/{id}")
    public PlantDTO getPlantById(@PathVariable Integer id) {
        return plantService.getPlantById(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Integer id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }

}

