package service;

import dto.PlantDTO;
import exception.GrowthStage;
import model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.*;
import reqResp.CreatePlantRequest;
import util.GrowthCalculator;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsabile della gestione della crescita delle piante.
 * Coordina:
 * - salvataggio entità
 * - aggiornamento crescita
 * - generazione forecast
 */
@Service
public class PlantService {

    private final PlantSpecieRepository plantSpecieRepository;
    private final PlantInstanceRepository plantInstanceRepository;
    private final GrowthForecastRepository growthForecastRepository;
    private final GrowthCalculator growthCalculator;
    private final LocationRepository locationRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;



    public PlantService(
            LocationRepository locationRepository,
            PlantSpecieRepository plantSpecieRepository,
            PlantInstanceRepository plantInstanceRepository,
            GrowthForecastRepository growthForecastRepository,
            RiskAssessmentRepository riskAssessmentRepository
    ) {

        this.locationRepository = locationRepository;
        this.plantSpecieRepository = plantSpecieRepository;
        this.plantInstanceRepository = plantInstanceRepository;
        this.growthForecastRepository = growthForecastRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.growthCalculator = new GrowthCalculator();


    }

    /**
     * Salva una nuova specie vegetale.
     */
    @Transactional
    public PlantSpecie salvaSpecie(PlantSpecie specie) {

        if (specie == null) {
            throw new IllegalArgumentException("PlantSpecie non può essere null");
        }

        return plantSpecieRepository.save(specie);
    }

    /**
     * Salva una nuova istanza di pianta (pianta reale).
     */
    public PlantInstance salvaPianta(PlantInstance pianta) {

        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (pianta.getPlantSpecie() == null) {
            throw new IllegalArgumentException("La pianta deve avere una specie associata");
        }

        if (pianta.getPlantSpecie().getSpecieId() == null) {
            throw new IllegalArgumentException("La specie deve essere salvata prima");
        }

        return plantInstanceRepository.save(pianta);
    }

    /**
     * Metodo principale di business:
     * - aggiorna la crescita della pianta
     * - calcola i nuovi indicatori
     * - genera un GrowthForecast
     */
    @Transactional
    public GrowthForecast aggiornaCrescitaEGeneraForecast(
            PlantInstance pianta,
            WeatherDay weatherDay,
            Double gddMedioPrevisto
    ) {

        // ===============================
        // VALIDAZIONE INPUT
        // ===============================
        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (weatherDay == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }

        if (pianta.getPlantSpecie() == null) {
            throw new IllegalArgumentException("La pianta deve avere una specie associata");
        }

        // ===============================
        // 1. AGGIORNAMENTO CRESCITA
        // ===============================
        // Aggiorna GDD accumulati e stadio della pianta
        growthCalculator.aggiornaCrescita(pianta, weatherDay);

        // Persistiamo lo stato aggiornato
        plantInstanceRepository.update(pianta);

        // ===============================
        // 2. CALCOLO INDICATORI
        // ===============================

        // GDD giornaliero
        Double gddGiornaliero = growthCalculator.calcolaGDDGiornaliero(
                pianta.getPlantSpecie(),
                weatherDay
        );

        // Percentuale di avanzamento del ciclo
        Double percentuale = growthCalculator.calcolaPercentualeCiclo(
                pianta.getPlantSpecie(),
                pianta.getStoreGDD()
        );

        // Giorni stimati alla maturazione
        Double gddMedioEffettivo = gddMedioPrevisto;

        if (gddMedioEffettivo == null || gddMedioEffettivo <= 0) {
            gddMedioEffettivo = gddGiornaliero;
        }

        Integer giorniAllaMaturazione =
                growthCalculator.stimaGiorniAllaMaturazione(
                        pianta,
                        gddMedioEffettivo
                );


        // ===============================
        // 3. CREAZIONE FORECAST
        // ===============================
        GrowthForecast forecast = new GrowthForecast();

        forecast.setPlantInstance(pianta);
        forecast.setDateTime(LocalDateTime.now());
        forecast.setGddPrevistiGiorn(gddGiornaliero);
        forecast.setPercentCiclo(percentuale);
        forecast.setStadioPrevisto(pianta.getGrowthStage());
        forecast.setGiorniNuovoStadio(giorniAllaMaturazione);

        // ===============================
        // 4. SALVATAGGIO
        // ===============================
        return growthForecastRepository.save(forecast);
    }

    @Transactional
    public PlantDTO createPlant(CreatePlantRequest request) {


        validateCreatePlantRequest(request);

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Location non trovata con : " + request.getLocationId()
                ));

        PlantSpecie specie = plantSpecieRepository.findById(request.getSpecieId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Specie non trovata con : " + request.getSpecieId()
                ));

        PlantInstance plant = new PlantInstance();
        plant.setNome(request.getNomePianta());
        plant.setLocation(location);
        plant.setPlantSpecie(specie);
        plant.setDataInsert(LocalDateTime.now());
        plant.setStoreGDD(0.0);
        plant.setGrowthStage(GrowthStage.SEMINA);
        plant.setNote(request.getNote());

        PlantInstance savedPlant = plantInstanceRepository.save(plant);

        return PlantDTO.fromEntity(savedPlant);
    }

    /**
     * Recupera tutte le piante associate a una location.
     */
    public List<PlantDTO> getPlantsByLocation(Integer locationId) {
        if (locationId == null) {
            throw new IllegalArgumentException("locationId è obbligatorio");
        }

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Location non trovata con id: " + locationId
                ));

        return plantInstanceRepository.findByLocation(location)
                .stream()
                .map(PlantDTO::fromEntity)
                .toList();
    }

    /**
     * Recupera una singola pianta per id.
     */
    public PlantDTO getPlantById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("id pianta obbligatorio");
        }

        PlantInstance plant = plantInstanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pianta non trovata con id: " + id
                ));

        return PlantDTO.fromEntity(plant);
    }
    @Transactional
    public void deletePlant(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id pianta obbligatorio");
        }

        PlantInstance plant = plantInstanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pianta non trovata con id: " + id
                ));

        growthForecastRepository.deleteByPlantInstance(plant);
        riskAssessmentRepository.deleteByPlantInstance(plant);

        plantInstanceRepository.deleteById(id);
    }


    private void validateCreatePlantRequest(CreatePlantRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        if (request.getLocationId() == null) {
            throw new IllegalArgumentException("La location è obbligatoria");
        }

        if (request.getSpecieId() == null) {
            throw new IllegalArgumentException("La specie è obbligatoria");
        }

        if (request.getNomePianta() == null || request.getNomePianta().isBlank()) {
            throw new IllegalArgumentException("Il nome della pianta è obbligatorio");
        }
    }
}