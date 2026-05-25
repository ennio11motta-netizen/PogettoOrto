package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class WeatherDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer weatherId;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalDateTime data;

    private Double tempMin;

    private Double tempMax;

    private Double umidita;

    private Double precipitazione;

    private Double ventoKmh;

    private Double uvIndex;


    public WeatherDay() {
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getPrecipitazione() {
        return precipitazione;
    }

    public void setPrecipitazione(Double precipitazione) {
        this.precipitazione = precipitazione;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getUmidita() {
        return umidita;
    }

    public void setUmidita(Double umidita) {
        this.umidita = umidita;
    }

    public Double getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(Double uvIndex) {
        this.uvIndex = uvIndex;
    }

    public Double getVentoKmh() {
        return ventoKmh;
    }

    public void setVentoKmh(Double ventoKmh) {
        this.ventoKmh = ventoKmh;
    }

    public Integer getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Integer weatherId) {
        this.weatherId = weatherId;
    }

    @Override
    public String toString() {
        return "WeatherDay{" +
                "data=" + data +
                ", weatherId=" + weatherId +
                ", location=" + location +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", umidita=" + umidita +
                ", precipitazione='" + precipitazione + '\'' +
                ", ventoKmh=" + ventoKmh +
                ", uvIndex=" + uvIndex +
                '}';
    }
}
