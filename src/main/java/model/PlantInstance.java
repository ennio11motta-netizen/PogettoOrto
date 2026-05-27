package model;

import exception.GrowthStage;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plant_instance")
public class PlantInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private Integer plantId;

    @ManyToOne
    @JoinColumn(name = "specie_id")
    private PlantSpecie plantSpecie;

    private LocalDateTime dataInsert;

    private Double storeGDD= 0.0;

    @Enumerated(EnumType.STRING)
    private GrowthStage growthStage;

    private String note;

//    //1 PlantInstance → N GrowthForecast
    @OneToMany(mappedBy = "plantInstance")
   private Set<GrowthForecast> growthForecasts = new HashSet<>();
//
//    //1 PlantInstance → N RiskAssessment
    @OneToMany(mappedBy = "plantInstance")
    private Set<RiskAssessment> riskAssessments =new  HashSet<>();



    public PlantInstance() {
    }


    public LocalDateTime getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(LocalDateTime dataInsert) {
        this.dataInsert = dataInsert;
    }

    public GrowthStage getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(GrowthStage growthStage) {
        this.growthStage = growthStage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public Double getStoreGDD() {
        return storeGDD;
    }

    public void setStoreGDD(Double storeGDD) {
        this.storeGDD = storeGDD;
    }


    public PlantSpecie getPlantSpecie() {
        return plantSpecie;
    }

    public void setPlantSpecie(PlantSpecie plantSpecie) {
        this.plantSpecie = plantSpecie;
    }


    @Override
    public String toString() {
        return "PlantInstance{" +
                "dataInsert=" + dataInsert +
                ", plantId=" + plantId +
                ", plantSpecie=" + plantSpecie +
                ", storeGDD=" + storeGDD +
                ", growthStage=" + growthStage +
                ", note='" + note + '\'' +
                '}';
    }
}
