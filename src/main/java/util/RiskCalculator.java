package util;

import config.RiskThresholdConfig;
import exception.RiskLevel;
import model.PlantInstance;
import model.PlantSpecie;
import model.RiskAssessment;
import model.WeatherDay;

public class RiskCalculator {

    private final RiskThresholdConfig config;

    public RiskCalculator() {
        this.config = new RiskThresholdConfig();
    }

    public RiskCalculator(RiskThresholdConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("RiskThresholdConfig non può essere null");
        }

        this.config = config;
    }

    public RiskAssessment generaValutazione(
            PlantInstance pianta,
            WeatherDay weatherDay
    ) {
        validateInput(pianta, weatherDay);

        PlantSpecie specie = pianta.getPlantSpecie();

        RiskAssessment assessment = new RiskAssessment();
        assessment.setPlantInstance(pianta);
        assessment.setWeatherDay(weatherDay);
        assessment.setDateTime(weatherDay.getData());

        RiskLevel riskCaldo = calcolaRischioCaldo(specie, weatherDay);
        RiskLevel riskFreddo = calcolaRischioFreddo(specie, weatherDay);
        RiskLevel riskVento = calcolaRischioVento(weatherDay);
        RiskLevel riskMalattia = calcolaRischioMalattia(specie, weatherDay);

        assessment.setRiskCaldo(riskCaldo);
        assessment.setRiskFreddo(riskFreddo);
        assessment.setRiskVento(riskVento);
        assessment.setRiskMalattia(riskMalattia);
        assessment.setConsigli(generaConsigli(
                riskCaldo,
                riskFreddo,
                riskVento,
                riskMalattia
        ));

        return assessment;
    }

    private RiskLevel calcolaRischioCaldo(
            PlantSpecie specie,
            WeatherDay weatherDay
    ) {
        Double tempMax = weatherDay.getTempMax();
        Double sogliaCaldo = specie.getSogliaStressCaldo();

        if (tempMax == null || sogliaCaldo == null) {
            return RiskLevel.LOW;
        }

        if (tempMax > sogliaCaldo + config.getDeltaCaldoHigh()) {
            return RiskLevel.HIGH;
        }

        if (tempMax >= sogliaCaldo) {
            return RiskLevel.MEDIUM;
        }

        return RiskLevel.LOW;
    }

    private RiskLevel calcolaRischioFreddo(
            PlantSpecie specie,
            WeatherDay weatherDay
    ) {
        Double tempMin = weatherDay.getTempMin();
        Double sogliaFreddo = specie.getSogliaStressFreddo();

        if (tempMin == null || sogliaFreddo == null) {
            return RiskLevel.LOW;
        }

        if (tempMin < sogliaFreddo - config.getDeltaFreddoHigh()) {
            return RiskLevel.HIGH;
        }

        if (tempMin <= sogliaFreddo) {
            return RiskLevel.MEDIUM;
        }

        return RiskLevel.LOW;
    }

    private RiskLevel calcolaRischioVento(WeatherDay weatherDay) {
        Double vento = weatherDay.getVentoKmh();

        if (vento == null) {
            return RiskLevel.LOW;
        }

        if (vento >= config.getVentoHighKmh()) {
            return RiskLevel.HIGH;
        }

        if (vento >= config.getVentoMediumKmh()) {
            return RiskLevel.MEDIUM;
        }

        return RiskLevel.LOW;
    }

    private RiskLevel calcolaRischioMalattia(
            PlantSpecie specie,
            WeatherDay weatherDay
    ) {
        Double umidita = weatherDay.getUmidita();
        Double precipitazione = weatherDay.getPrecipitazione();
        Double tempMin = weatherDay.getTempMin();
        Double tempMax = weatherDay.getTempMax();

        if (umidita == null || tempMin == null || tempMax == null) {
            return RiskLevel.LOW;
        }

        boolean specieSensibile = isSpecieSensibileAMalattie(specie);

        if (!specieSensibile) {
            return RiskLevel.LOW;
        }

        Double tempMedia = (tempMin + tempMax) / 2.0;

        boolean temperaturaFavorevole =
                tempMedia >= config.getTemperaturaMalattiaMin()
                        && tempMedia <= config.getTemperaturaMalattiaMax();

        boolean pioggiaPresente =
                precipitazione != null
                        && precipitazione >= config.getPrecipitazioneMalattiaMm();

        if (umidita >= config.getUmiditaHigh()
                && temperaturaFavorevole
                && pioggiaPresente) {
            return RiskLevel.HIGH;
        }

        if (umidita >= config.getUmiditaMedium()
                && temperaturaFavorevole) {
            return RiskLevel.MEDIUM;
        }

        return RiskLevel.LOW;
    }

    private boolean isSpecieSensibileAMalattie(PlantSpecie specie) {
        if (specie == null || specie.getSensibilitaMalattie() == null) {
            return false;
        }

        String sensibilita = specie.getSensibilitaMalattie().trim();

        return !sensibilita.isBlank()
                && !sensibilita.equalsIgnoreCase("nessuna")
                && !sensibilita.equalsIgnoreCase("no");
    }

    private String generaConsigli(
            RiskLevel caldo,
            RiskLevel freddo,
            RiskLevel vento,
            RiskLevel malattia
    ) {
        StringBuilder consigli = new StringBuilder();

        if (caldo == RiskLevel.HIGH) {
            consigli.append("Rischio caldo alto: aumentare irrigazione e ombreggiare la pianta. ");
        } else if (caldo == RiskLevel.MEDIUM) {
            consigli.append("Rischio caldo medio: monitorare idratazione e temperatura. ");
        }

        if (freddo == RiskLevel.HIGH) {
            consigli.append("Rischio freddo alto: proteggere la pianta con copertura. ");
        } else if (freddo == RiskLevel.MEDIUM) {
            consigli.append("Rischio freddo medio: monitorare le temperature minime. ");
        }

        if (vento == RiskLevel.HIGH) {
            consigli.append("Rischio vento alto: fissare o proteggere le piante più esposte. ");
        } else if (vento == RiskLevel.MEDIUM) {
            consigli.append("Rischio vento medio: controllare sostegni e stabilità. ");
        }

        if (malattia == RiskLevel.HIGH) {
            consigli.append("Rischio malattia alto: condizioni favorevoli a patogeni, valutare prevenzione. ");
        } else if (malattia == RiskLevel.MEDIUM) {
            consigli.append("Rischio malattia medio: monitorare foglie e umidità. ");
        }

        if (consigli.isEmpty()) {
            return "Nessun rischio rilevante.";
        }

        return consigli.toString().trim();
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

        if (pianta.getPlantSpecie() == null) {
            throw new IllegalArgumentException("La pianta deve avere una specie associata");
        }
    }
}