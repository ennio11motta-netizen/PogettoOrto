package dto;
import java.time.LocalDate;

public class WeatherApiDTO {

    private LocalDate date;
    private Double tempMin;
    private Double tempMax;
    private Double umidita;
    private Double precipitazione;
    private Double ventoKmh;
    private Double uvIndex;

    public WeatherApiDTO() {
    }

    public WeatherApiDTO(
            LocalDate date,
            Double tempMin,
            Double tempMax,
            Double umidita,
            Double precipitazione,
            Double ventoKmh,
            Double uvIndex
    ) {
        this.date = date;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.umidita = umidita;
        this.precipitazione = precipitazione;
        this.ventoKmh = ventoKmh;
        this.uvIndex = uvIndex;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getUmidita() {
        return umidita;
    }

    public void setUmidita(Double umidita) {
        this.umidita = umidita;
    }

    public Double getPrecipitazione() {
        return precipitazione;
    }

    public void setPrecipitazione(Double precipitazione) {
        this.precipitazione = precipitazione;
    }

    public Double getVentoKmh() {
        return ventoKmh;
    }

    public void setVentoKmh(Double ventoKmh) {
        this.ventoKmh = ventoKmh;
    }

    public Double getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(Double uvIndex) {
        this.uvIndex = uvIndex;
    }

    @Override
    public String toString() {
        return "WeatherApiDTO{" +
                "date=" + date +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", umidita=" + umidita +
                ", precipitazione=" + precipitazione +
                ", ventoKmh=" + ventoKmh +
                ", uvIndex=" + uvIndex +
                '}';
    }
}