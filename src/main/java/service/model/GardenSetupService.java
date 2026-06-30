
package service.model;
import model.data.Location;
import model.data.PlantInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.*;
import reqResp.CreateGardenRequest;
import reqResp.GardenResponse;
import java.util.List;
import service.rdf.RdfService;

@Service
public class GardenSetupService {

    private final LocationRepository locationRepository;
    private final PlantInstanceRepository plantInstanceRepository;
    private final WeatherDayRepository weatherDayRepository;
    private final GrowthForecastRepository growthForecastRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final RdfService rdfService;


    public GardenSetupService(
            LocationRepository locationRepository,
            PlantInstanceRepository plantInstanceRepository,
            WeatherDayRepository weatherDayRepository,
            GrowthForecastRepository growthForecastRepository,
            RiskAssessmentRepository riskAssessmentRepository,
            RdfService rdfService
    ) {
        this.locationRepository = locationRepository;
        this.plantInstanceRepository = plantInstanceRepository;
        this.weatherDayRepository = weatherDayRepository;
        this.growthForecastRepository = growthForecastRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;

        this.rdfService = rdfService;

    }


    @Transactional
    public GardenResponse createGarden(CreateGardenRequest request) {
        validateCreateGardenRequest(request);

        Location location = new Location();
        location.setNome(request.getNomeOrto());
        location.setLatitudine(request.getLatitudine());
        location.setLongitudine(request.getLongitudine());

        Location savedLocation = locationRepository.save(location);

        rdfService.exportGarden(savedLocation);
        rdfService.persist();

        return new GardenResponse(
                savedLocation.getLocationId(),
                savedLocation.getNome(),
                savedLocation.getLatitudine(),
                savedLocation.getLongitudine()
        );
    }
    @Transactional
    public void deleteGarden(Integer locationId) {
        if (locationId == null) {
            throw new IllegalArgumentException("Id orto obbligatorio");
        }

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Orto non trovato con id: " + locationId
                ));

        List<PlantInstance> plants = plantInstanceRepository.findByLocation(location);

        for (PlantInstance plant : plants) {
            growthForecastRepository.deleteByPlantInstance(plant);
            riskAssessmentRepository.deleteByPlantInstance(plant);
            plantInstanceRepository.deleteById(plant.getPlantId());
        }

        weatherDayRepository.deleteByLocation(location);

        locationRepository.deleteById(locationId);
    }

    private void validateCreateGardenRequest(CreateGardenRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        if (request.getNomeOrto() == null || request.getNomeOrto().isBlank()) {
            throw new IllegalArgumentException("Il nome dell'orto è obbligatorio");
        }

        if (request.getLatitudine() == null) {
            throw new IllegalArgumentException("La latitudine è obbligatoria");
        }

        if (request.getLongitudine() == null) {
            throw new IllegalArgumentException("La longitudine è obbligatoria");
        }

        if (request.getLatitudine() < -90 || request.getLatitudine() > 90) {
            throw new IllegalArgumentException("Latitudine non valida");
        }

        if (request.getLongitudine() < -180 || request.getLongitudine() > 180) {
            throw new IllegalArgumentException("Longitudine non valida");
        }
    }

    @Transactional(readOnly = true)
    public List<GardenResponse> getAllGardens() {
        return locationRepository.findAll()
                .stream()
                .map(location -> new GardenResponse(
                        location.getLocationId(),
                        location.getNome(),
                        location.getLatitudine(),
                        location.getLongitudine()
                ))
                .toList();
    }



}