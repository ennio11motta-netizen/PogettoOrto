package service.model;

import model.data.PlantInstance;
import model.data.RiskAssessment;
import model.data.WeatherDay;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.RiskAssessmentRepository;
import util.RiskCalculator;

/**
 * Service responsabile della valutazione del rischio.
 * Responsabilità:
 * - validare input applicativi
 * - usare RiskCalculator per generare la valutazione
 * - delegare persistenza e deduplicazione al repository
 */
@Service
public class RiskService {

    private final RiskAssessmentRepository riskAssessmentRepository;
    private final RiskCalculator riskCalculator;

    public RiskService(
            RiskAssessmentRepository riskAssessmentRepository,
            RiskCalculator riskCalculator
    ) {
        if (riskAssessmentRepository == null) {
            throw new IllegalArgumentException("RiskAssessmentRepository non può essere null");
        }
        if (riskCalculator == null) {
            throw new IllegalArgumentException("RiskCalculator non può essere null");
        }
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.riskCalculator = riskCalculator;
    }
    @Transactional
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
    /*
     * PREVIEW
     */
    public RiskAssessment generaValutazionePreview(
            PlantInstance pianta,
            WeatherDay weatherDay
    ) {
        validateInput(pianta, weatherDay);

        /*
         * Calcolo puro della valutazione.
         * Non salviamo nulla nel DB.
         */
        RiskAssessment assessment =
                riskCalculator.generaValutazione(pianta, weatherDay);

        if (assessment.getDateTime() == null && weatherDay.getData() != null) {
            assessment.setDateTime(weatherDay.getData());
        }

        return assessment;
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