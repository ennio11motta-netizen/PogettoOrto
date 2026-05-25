package config;



/**
 * Configurazione delle soglie generali usate dal RiskCalculator.
 *
 * Le soglie biologiche specie-specifiche restano in PlantSpecie:
 * - sogliaStressCaldo
 * - sogliaStressFreddo
 *
 * Qui invece mettiamo soglie generali ambientali:
 * - vento
 * - umidità
 * - precipitazione
 * - range temperatura per rischio malattia
 * - delta per rischio caldo/freddo alto
 */
public class RiskThresholdConfig {

    private Double deltaCaldoHigh = 3.0;
    private Double deltaFreddoHigh = 3.0;

    private Double ventoMediumKmh = 30.0;
    private Double ventoHighKmh = 60.0;

    private Double umiditaMedium = 80.0;
    private Double umiditaHigh = 90.0;

    private Double precipitazioneMalattiaMm = 1.0;

    private Double temperaturaMalattiaMin = 15.0;
    private Double temperaturaMalattiaMax = 22.0;

    public RiskThresholdConfig() {
    }

    public Double getDeltaCaldoHigh() {
        return deltaCaldoHigh;
    }

    public void setDeltaCaldoHigh(Double deltaCaldoHigh) {
        this.deltaCaldoHigh = deltaCaldoHigh;
    }

    public Double getDeltaFreddoHigh() {
        return deltaFreddoHigh;
    }

    public void setDeltaFreddoHigh(Double deltaFreddoHigh) {
        this.deltaFreddoHigh = deltaFreddoHigh;
    }

    public Double getVentoMediumKmh() {
        return ventoMediumKmh;
    }

    public void setVentoMediumKmh(Double ventoMediumKmh) {
        this.ventoMediumKmh = ventoMediumKmh;
    }

    public Double getVentoHighKmh() {
        return ventoHighKmh;
    }

    public void setVentoHighKmh(Double ventoHighKmh) {
        this.ventoHighKmh = ventoHighKmh;
    }

    public Double getUmiditaMedium() {
        return umiditaMedium;
    }

    public void setUmiditaMedium(Double umiditaMedium) {
        this.umiditaMedium = umiditaMedium;
    }

    public Double getUmiditaHigh() {
        return umiditaHigh;
    }

    public void setUmiditaHigh(Double umiditaHigh) {
        this.umiditaHigh = umiditaHigh;
    }

    public Double getPrecipitazioneMalattiaMm() {
        return precipitazioneMalattiaMm;
    }

    public void setPrecipitazioneMalattiaMm(Double precipitazioneMalattiaMm) {
        this.precipitazioneMalattiaMm = precipitazioneMalattiaMm;
    }

    public Double getTemperaturaMalattiaMin() {
        return temperaturaMalattiaMin;
    }

    public void setTemperaturaMalattiaMin(Double temperaturaMalattiaMin) {
        this.temperaturaMalattiaMin = temperaturaMalattiaMin;
    }

    public Double getTemperaturaMalattiaMax() {
        return temperaturaMalattiaMax;
    }

    public void setTemperaturaMalattiaMax(Double temperaturaMalattiaMax) {
        this.temperaturaMalattiaMax = temperaturaMalattiaMax;
    }
}