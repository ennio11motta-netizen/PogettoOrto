package controller;

import org.springframework.http.ResponseEntity;
import reqResp.CreateGardenRequest;
import reqResp.GardenResponse;

import org.springframework.web.bind.annotation.*;
import service.GardenSetupService;

import java.util.List;


@RestController
@RequestMapping("/api/garden")
@CrossOrigin(origins = "http://localhost:5173")
public class GardenSetupController {


    private final GardenSetupService gardenSetupService;



    public GardenSetupController(GardenSetupService gardenSetupService) {
        this.gardenSetupService = gardenSetupService;
    }



    @PostMapping
    public GardenResponse createGarden(@RequestBody CreateGardenRequest request) {
        return gardenSetupService.createGarden(request);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarden(@PathVariable Integer id) {
        gardenSetupService.deleteGarden(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public List<GardenResponse> getAllGardens() {
        return gardenSetupService.getAllGardens();
    }

}
