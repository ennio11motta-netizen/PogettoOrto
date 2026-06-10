package model;

import exception.GrowthStage;
import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "growth_forecast")
public class GrowthForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forecast_id")
    private Integer forecastId;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private PlantInstance plantInstance;

    private LocalDateTime dateTime;
    private Double gddPrevistiGiorn;
    private Double percentCiclo;

    @Enumerated(EnumType.STRING)
    private GrowthStage stadioPrevisto;

    private Integer giorniNuovoStadio;



//    public LocalDateTime getDateTime() {
//        return dateTime;
//    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getForecastId() {
        return forecastId;
    }

    public void setForecastId(Integer forecastId) {
        this.forecastId = forecastId;
    }

   public Double getGddPrevistiGiorn() {
       return gddPrevistiGiorn;
    }

    public void setGddPrevistiGiorn(Double gddPrevistiGiorn) {
        this.gddPrevistiGiorn = gddPrevistiGiorn;
    }

    public Integer getGiorniNuovoStadio() {
        return giorniNuovoStadio;
   }

    public void setGiorniNuovoStadio(Integer giorniNuovoStadio) {
        this.giorniNuovoStadio = giorniNuovoStadio;
    }

    public Double getPercentCiclo() {
        return percentCiclo;
    }

    public void setPercentCiclo(Double percentCiclo) {
        this.percentCiclo = percentCiclo;
    }


    public void setPlantInstance(PlantInstance plantInstance) {
        this.plantInstance = plantInstance;
    }

    public void setStadioPrevisto(GrowthStage stadioPrevisto) {
        this.stadioPrevisto = stadioPrevisto;
    }


    public GrowthStage getStadioPrevisto() {
        return stadioPrevisto;
    }


    public GrowthForecast() {
    }



    @Override
    public String toString() {
        return "GrowthForecast{" +
                "dateTime=" + dateTime +
                ", forecastId=" + forecastId +
//                ", plantInstance=" + plantInstance +
                ", gddPrevistiGiorn=" + gddPrevistiGiorn +
                ", percentCiclo=" + percentCiclo +
                ", stadioPrevisto=" + stadioPrevisto +
                ", giorniNuovoStadio=" + giorniNuovoStadio +
                '}';
    }
}
