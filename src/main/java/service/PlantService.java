package service;

import jakarta.persistence.EntityManager;
import model.*;
import repository.GrowthForecastRepository;
import repository.PlantInstanceRepository;
import repository.PlantSpecieRepository;
import util.GrowthCalculator;

import java.time.LocalDateTime;

/**
 * Service responsabile della gestione della crescita delle piante.
 * Coordina:
 * - salvataggio entità
 * - aggiornamento crescita
 * - generazione forecast
 */
public class PlantService {

    private final PlantSpecieRepository plantSpecieRepository;
    private final PlantInstanceRepository plantInstanceRepository;
    private final GrowthForecastRepository growthForecastRepository;
    private final GrowthCalculator growthCalculator;

    public PlantService(EntityManager em) {
        this.plantSpecieRepository = new PlantSpecieRepository(em);
        this.plantInstanceRepository = new PlantInstanceRepository(em);
        this.growthForecastRepository = new GrowthForecastRepository(em);
        this.growthCalculator = new GrowthCalculator();
    }

    /**
     * Salva una nuova specie vegetale.
     */
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
}