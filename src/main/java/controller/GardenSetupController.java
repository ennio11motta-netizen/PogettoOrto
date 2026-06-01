package controller;

import reqResp.GardenSetupRequest;
import reqResp.GardenSetupResponse;

import org.springframework.web.bind.annotation.*;
import service.GardenSetupService;


@RestController
@RequestMapping("/api/garden")
@CrossOrigin(origins = "http://localhost:5173")
public class GardenSetupController {


    private final GardenSetupService gardenSetupService;



    public GardenSetupController(GardenSetupService gardenSetupService) {
        this.gardenSetupService = gardenSetupService;
    }


    @PostMapping("/setup")
    public GardenSetupResponse setupGarden(@RequestBody GardenSetupRequest request) {
        return gardenSetupService.setupGarden(request);
    }
}
