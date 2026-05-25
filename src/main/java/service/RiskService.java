package service;

import jakarta.persistence.EntityManager;
import model.PlantInstance;
import model.RiskAssessment;
import model.WeatherDay;
import repository.RiskAssessmentRepository;
import util.RiskCalculator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service responsabile della valutazione del rischio.
 *
 * Responsabilità:
 * - validare input applicativi
 * - usare RiskCalculator per generare la valutazione
 * - delegare persistenza e deduplicazione al repository
 */
public class RiskService {

    private final RiskAssessmentRepository riskAssessmentRepository;
    private final RiskCalculator riskCalculator;

    public RiskService(EntityManager em) {
        this.riskAssessmentRepository = new RiskAssessmentRepository(em);
        this.riskCalculator = new RiskCalculator();
    }

    public RiskAssessment generaESalvaValutazione(
            PlantInstance pianta,
            WeatherDay weatherDay
    ) {
        validateInput(pianta, weatherDay);

        RiskAssessment assessment =
                riskCalculator.generaValutazione(pianta, weatherDay);

        if (assessment.getDateTime() == null && weatherDay.getData() != null) {
            assessment.setDateTime(weatherDay.getData());
        }

        return riskAssessmentRepository.saveOrUpdateByPlantAndDate(assessment);
    }

    public List<RiskAssessment> getByPlantInstance(PlantInstance pianta) {
        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        return riskAssessmentRepository.findByPlantInstanceOrderByDateAsc(pianta);
    }

    public Optional<RiskAssessment> getByPlantInstanceAndDate(
            PlantInstance pianta,
            LocalDateTime data
    ) {
        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (data == null) {
            throw new IllegalArgumentException("La data non può essere null");
        }

        return riskAssessmentRepository.findByPlantInstanceAndDate(pianta, data);
    }

    private void validateInput(
            PlantInstance pianta,
            WeatherDay weatherDay
    ) {
        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (weatherDay == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }
    }
}