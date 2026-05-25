package model;

import exception.RiskLevel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RiskAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer riskId;

    @ManyToOne
    @JoinColumn(name = "plant_instance_id")
    private PlantInstance plantInstance;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskCaldo;
    @Enumerated(EnumType.STRING)
    private RiskLevel riskFreddo;
    @Enumerated(EnumType.STRING)
    private RiskLevel riskVento;
    @Enumerated(EnumType.STRING)
    private RiskLevel riskMalattia;

    private String consigli;


    public String getConsigli() {
        return consigli;
    }

    public void setConsigli(String consigli) {
        this.consigli = consigli;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public PlantInstance getPlantInstance() {
        return plantInstance;
    }

    public void setPlantInstance(PlantInstance plantInstance) {
        this.plantInstance = plantInstance;
    }

    public RiskLevel getRiskCaldo() {
        return riskCaldo;
    }

    public void setRiskCaldo(RiskLevel riskCaldo) {
        this.riskCaldo = riskCaldo;
    }

    public RiskLevel getRiskFreddo() {
        return riskFreddo;
    }

    public void setRiskFreddo(RiskLevel riskFreddo) {
        this.riskFreddo = riskFreddo;
    }

    public Integer getRiskId() {
        return riskId;
    }

    public void setRiskId(Integer riskId) {
        this.riskId = riskId;
    }

    public RiskLevel getRiskMalattia() {
        return riskMalattia;
    }

    public void setRiskMalattia(RiskLevel riskMalattia) {
        this.riskMalattia = riskMalattia;
    }

    public RiskLevel getRiskVento() {
        return riskVento;
    }

    public void setRiskVento(RiskLevel riskVento) {
        this.riskVento = riskVento;
    }

    public RiskAssessment(String consigli, LocalDateTime dateTime, PlantInstance plantInstance, RiskLevel riskCaldo, RiskLevel riskFreddo, Long riskId, RiskLevel riskMalattia, RiskLevel riskVento) {
        this.consigli = consigli;
        this.dateTime = dateTime;
        this.plantInstance = plantInstance;
        this.riskCaldo = riskCaldo;
        this.riskFreddo = riskFreddo;
        this.riskMalattia = riskMalattia;
        this.riskVento = riskVento;
    }

    public RiskAssessment() {
    }


    @Override
    public String toString() {
        return "RiskAssessment{" +
                "consigli='" + consigli + '\'' +
                ", riskId=" + riskId +
                ", plantInstance=" + plantInstance +
                ", dateTime=" + dateTime +
                ", riskCaldo=" + riskCaldo +
                ", riskFreddo=" + riskFreddo +
                ", riskVento=" + riskVento +
                ", riskMalattia=" + riskMalattia +
                '}';
    }
}
