package controller;


import org.springframework.web.bind.annotation.*;
import dto.rdf.RdfGardenDetailDTO;
//import dto.rdf.RdfSimulationRunDTO;
//import dto.rdf.RdfSimulationRunDetailDTO;
import dto.rdf.RdfSimulationRunHistoryDTO;
import service.rdf.RdfGardenDetailService;
import service.rdf.RdfSimulationRunService;

import java.util.List;

@RestController
@RequestMapping("/api/rdf")
@CrossOrigin(origins = "http://localhost:5173")
public class RdfController {

    private final RdfGardenDetailService rdfGardenDetailService;
    private final RdfSimulationRunService rdfSimulationRunService;

    public RdfController(RdfGardenDetailService rdfGardenDetailService,
                         RdfSimulationRunService rdfSimulationRunService) {
        this.rdfGardenDetailService = rdfGardenDetailService;
        this.rdfSimulationRunService= rdfSimulationRunService;
    }

    @GetMapping("/garden-detail/{locationId}")
    public RdfGardenDetailDTO getGardenDetail(@PathVariable Integer locationId) {
        return rdfGardenDetailService.getGardenDetail(locationId);
    }


//    @Deprecated
//    @GetMapping("/simulation-runs/{locationId}")
//    public List<RdfSimulationRunDTO> getSimulationRuns(
//            @PathVariable Integer locationId
//    ) {
//        return rdfSimulationRunService.getSimulationRunsByGarden(locationId);
//    }

//    @Deprecated
//    @GetMapping("/simulation-run-detail/{runId}")
//    public RdfSimulationRunDetailDTO getSimulationRunDetail(
//            @PathVariable String runId
//    ) {
//        return rdfSimulationRunService.getSimulationRunDetail(runId);
//    }

    @GetMapping("/simulation-history/{locationId}")
    public List<RdfSimulationRunHistoryDTO> getSimulationHistory(
            @PathVariable Integer locationId
    ) {
        return rdfSimulationRunService.getSimulationHistoryByGarden(locationId);
    }
}
