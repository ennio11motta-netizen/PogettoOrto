package service.rdf;

import exception.GrowthStage;
import exception.RiskLevel;
import model.data.GrowthForecast;
import model.data.Location;
import model.data.PlantInstance;
import model.data.RiskAssessment;
import model.data.WeatherDay;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static model.rdf.RdfVocabulary.*;

@Service
public class RdfExportService {

    // ===============================
    // EXPORT GARDEN
    // ===============================

    public void exportGarden(Model model, Location location) {
        validateModel(model);

        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (location.getLocationId() == null) {
            throw new IllegalArgumentException("Location deve avere un id");
        }

        Resource garden =
                model.createResource(NS + "garden/" + location.getLocationId());

        garden.addProperty(RDF.type, GARDEN_CLASS);

        if (location.getNome() != null) {
            garden.addProperty(NAME, location.getNome());
        }

        if (location.getLatitudine() != null) {
            garden.addLiteral(LATITUDE, location.getLatitudine());
        }

        if (location.getLongitudine() != null) {
            garden.addLiteral(LONGITUDE, location.getLongitudine());
        }
    }

    // ===============================
    // EXPORT PLANT
    // ===============================

    public void exportPlantInstance(Model model, PlantInstance pianta) {
        validateModel(model);

        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (pianta.getPlantId() == null) {
            throw new IllegalArgumentException("PlantInstance deve avere un id");
        }

        Resource plant =
                model.createResource(NS + "plant/" + pianta.getPlantId());

        removePlantMutableTriples(model, plant);

        plant.addProperty(RDF.type, PLANT_INSTANCE_CLASS);
        plant.addProperty(RDF.type, PLANT_CLASS);

        if (pianta.getNome() != null) {
            plant.addProperty(NAME, pianta.getNome());
        }

        if (pianta.getNote() != null) {
            plant.addProperty(NOTE, pianta.getNote());
        }

        if (
                pianta.getPlantSpecie() != null &&
                        pianta.getPlantSpecie().getNome() != null
        ) {
            plant.addProperty(SPECIES_NAME, pianta.getPlantSpecie().getNome());
        }

        if (pianta.getStoreGDD() != null) {
            plant.addLiteral(STORE_GDD, pianta.getStoreGDD());
        }

        if (pianta.getGrowthStage() != null) {
            GrowthStage stage = pianta.getGrowthStage();

            plant.addProperty(GROWTH_STAGE, stage.name());

            if (isAdvancedStage(stage)) {
                plant.addProperty(RDF.type, ADVANCED_STAGE);
            } else if (isEarlyStage(stage)) {
                plant.addProperty(RDF.type, EARLY_STAGE);
            }
        }
    }

    // ===============================
    // EXPORT WEATHER
    // ===============================

    public void exportWeatherDay(Model model, WeatherDay wd) {
        validateModel(model);

        if (wd == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }

        if (wd.getWeatherId() == null) {
            throw new IllegalArgumentException("WeatherDay deve avere un id");
        }

        Resource weather =
                model.createResource(NS + "weather/" + wd.getWeatherId());

        weather.addProperty(RDF.type, WEATHER_CLASS);

        if (wd.getData() != null) {
            weather.addProperty(DATE_TIME, wd.getData().toString());
        }

        if (wd.getTempMax() != null) {
            weather.addLiteral(TEMP_MAX, wd.getTempMax());
        }

        if (wd.getTempMin() != null) {
            weather.addLiteral(TEMP_MIN, wd.getTempMin());
        }

        if (wd.getUmidita() != null) {
            weather.addLiteral(UMIDITA, wd.getUmidita());
        }

        if (wd.getPrecipitazione() != null) {
            weather.addLiteral(PRECIPITAZIONE, wd.getPrecipitazione());
        }

        if (wd.getVentoKmh() != null) {
            weather.addLiteral(WIND_KMH, wd.getVentoKmh());
        }

        if (wd.getUvIndex() != null) {
            weather.addLiteral(UV_INDEX, wd.getUvIndex());
        }
    }

    // ===============================
    // EXPORT RISK
    // ===============================

    public void exportRiskAssessment(Model model, RiskAssessment risk) {
        validateModel(model);

        if (risk == null) {
            throw new IllegalArgumentException("RiskAssessment non può essere null");
        }

        if (risk.getRiskId() == null) {
            throw new IllegalArgumentException("RiskAssessment deve avere un id");
        }

        Resource riskRes =
                model.createResource(NS + "risk/" + risk.getRiskId());

        riskRes.addProperty(RDF.type, RISK_ASSESSMENT_CLASS);
        riskRes.addProperty(RDF.type, RISK_CLASS);

        if (risk.getRiskCaldo() != null) {
            riskRes.addProperty(RISK_CALDO, risk.getRiskCaldo().name());
        }

        if (risk.getRiskFreddo() != null) {
            riskRes.addProperty(RISK_FREDDO, risk.getRiskFreddo().name());
        }

        if (risk.getRiskVento() != null) {
            riskRes.addProperty(RISK_VENTO, risk.getRiskVento().name());
        }

        if (risk.getRiskMalattia() != null) {
            riskRes.addProperty(RISK_MALATTIA, risk.getRiskMalattia().name());
        }

        if (risk.getConsigli() != null) {
            riskRes.addProperty(CONSIGLI, risk.getConsigli());
        }

        boolean isHigh =
                risk.getRiskCaldo() == RiskLevel.HIGH ||
                        risk.getRiskFreddo() == RiskLevel.HIGH ||
                        risk.getRiskVento() == RiskLevel.HIGH ||
                        risk.getRiskMalattia() == RiskLevel.HIGH;

        boolean isMedium =
                risk.getRiskCaldo() == RiskLevel.MEDIUM ||
                        risk.getRiskFreddo() == RiskLevel.MEDIUM ||
                        risk.getRiskVento() == RiskLevel.MEDIUM ||
                        risk.getRiskMalattia() == RiskLevel.MEDIUM;

        if (isHigh) {
            riskRes.addProperty(RDF.type, CRITICAL_RISK);
        } else if (isMedium) {
            riskRes.addProperty(RDF.type, MODERATE_RISK);
        } else {
            riskRes.addProperty(RDF.type, LOW_RISK);
        }
    }

    // ===============================
    // EXPORT FORECAST
    // ===============================

    public void exportGrowthForecast(Model model, GrowthForecast forecast) {
        validateModel(model);

        if (forecast == null) {
            throw new IllegalArgumentException("GrowthForecast non può essere null");
        }

        if (forecast.getForecastId() == null) {
            throw new IllegalArgumentException("GrowthForecast deve avere un id");
        }

        Resource forecastRes =
                model.createResource(NS + "forecast/" + forecast.getForecastId());

        forecastRes.addProperty(RDF.type, FORECAST_CLASS);

        if (forecast.getDateTime() != null) {
            forecastRes.addProperty(DATE_TIME, forecast.getDateTime().toString());
        }

        if (forecast.getGddPrevistiGiorn() != null) {
            forecastRes.addLiteral(GDD_DAILY, forecast.getGddPrevistiGiorn());
        }

        if (forecast.getPercentCiclo() != null) {
            forecastRes.addLiteral(PERCENT_CICLO, forecast.getPercentCiclo());
        }

        if (forecast.getStadioPrevisto() != null) {
            forecastRes.addProperty(
                    GROWTH_STAGE,
                    forecast.getStadioPrevisto().name()
            );
        }

        if (forecast.getGiorniNuovoStadio() != null) {
            forecastRes.addLiteral(
                    DAYS_TO_MATURITY,
                    forecast.getGiorniNuovoStadio()
            );
        }
    }

    // ===============================
    // EXPORT SIMULATION RUN
    // ===============================

    public String exportSimulationRun(
            Model model,
            Location location,
            String mode
    ) {
        validateModel(model);

        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (location.getLocationId() == null) {
            throw new IllegalArgumentException("Location deve avere un id");
        }

        if (mode == null || mode.isBlank()) {
            throw new IllegalArgumentException("Mode simulazione obbligatoria");
        }

        String normalizedMode = mode.trim().toUpperCase();

        if (!normalizedMode.equals("PREVIEW") && !normalizedMode.equals("APPLIED")) {
            throw new IllegalArgumentException("Mode simulazione non valida: " + mode);
        }

        String runId = location.getLocationId() + "-" + System.currentTimeMillis();

        Resource simulation =
                model.createResource(NS + "simulation/" + runId);

        Resource garden =
                model.createResource(NS + "garden/" + location.getLocationId());

        simulation.addProperty(RDF.type, SIMULATION_RUN_CLASS);

        if (normalizedMode.equals("PREVIEW")) {
            simulation.addProperty(RDF.type, PREVIEW_SIMULATION_CLASS);
        }

        if (normalizedMode.equals("APPLIED")) {
            simulation.addProperty(RDF.type, APPLIED_SIMULATION_CLASS);
        }

        simulation.addProperty(MODE, normalizedMode);
        simulation.addProperty(CREATED_AT, LocalDateTime.now().toString());
        simulation.addProperty(FOR_GARDEN, garden);

        return simulation.getURI();
    }

    // ===============================
    // HELPER PRIVATI
    // ===============================

    private void validateModel(Model model) {
        if (model == null) {
            throw new IllegalArgumentException("Model RDF non può essere null");
        }
    }

    private void removePlantMutableTriples(Model model, Resource plant) {
        model.removeAll(plant, NAME, null);
        model.removeAll(plant, NOTE, null);
        model.removeAll(plant, SPECIES_NAME, null);
        model.removeAll(plant, STORE_GDD, null);
        model.removeAll(plant, GROWTH_STAGE, null);

        model.removeAll(plant, RDF.type, EARLY_STAGE);
        model.removeAll(plant, RDF.type, ADVANCED_STAGE);
    }

    private boolean isAdvancedStage(GrowthStage stage) {
        return stage == GrowthStage.FIORITURA ||
                stage == GrowthStage.FRUTTIFICAZIONE ||
                stage == GrowthStage.MATURAZIONE;
    }

    private boolean isEarlyStage(GrowthStage stage) {
        return stage == GrowthStage.SEMINA ||
                stage == GrowthStage.EMERGENZA ||
                stage == GrowthStage.VEGETATIVO;
    }
}