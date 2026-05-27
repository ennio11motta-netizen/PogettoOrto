package controller;

public class SimulationStepDTO {

    // ===============================
    // DATI METEO
    // ===============================
    private Integer weatherId;
    private String date;

    private Double tempMin;
    private Double tempMax;
    private Double umidita;
    private Double precipitazione;
    private Double ventoKmh;
    private Double uvIndex;

    // ===============================
    // DATI PIANTA / CRESCITA
    // ===============================
    private Integer plantId;
    private Double storeGDD;
    private String growthStage;

    private Integer forecastId;
    private Double gddGiornaliero;
    private Double percentCiclo;
    private Integer giorniAllaMaturazione;

    // ===============================
    // DATI RISCHIO
    // ===============================
    private Integer riskId;
    private String riskCaldo;
    private String riskFreddo;
    private String riskVento;
    private String riskMalattia;
    private String consigli;

    public SimulationStepDTO() {
    }

    public Integer getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Integer weatherId) {
        this.weatherId = weatherId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }

    public Integer getForecastId() {
        return forecastId;
    }

    public void setForecastId(Integer forecastId) {
        this.forecastId = forecastId;
    }

    public Double getGddGiornaliero() {
        return gddGiornaliero;
    }

    public void setGddGiornaliero(Double gddGiornaliero) {
        this.gddGiornaliero = gddGiornaliero;
    }

    public Double getPercentCiclo() {
        return percentCiclo;
    }

    public void setPercentCiclo(Double percentCiclo) {
        this.percentCiclo = percentCiclo;
    }

    public Integer getGiorniAllaMaturazione() {
        return giorniAllaMaturazione;
    }

    public void setGiorniAllaMaturazione(Integer giorniAllaMaturazione) {
        this.giorniAllaMaturazione = giorniAllaMaturazione;
    }

    public Integer getRiskId() {
        return riskId;
    }

    public void setRiskId(Integer riskId) {
        this.riskId = riskId;
    }

    public String getRiskCaldo() {
        return riskCaldo;
    }

    public void setRiskCaldo(String riskCaldo) {
        this.riskCaldo = riskCaldo;
    }

    public String getRiskFreddo() {
        return riskFreddo;
    }

    public void setRiskFreddo(String riskFreddo) {
        this.riskFreddo = riskFreddo;
    }

    public String getRiskVento() {
        return riskVento;
    }

    public void setRiskVento(String riskVento) {
        this.riskVento = riskVento;
    }

    public String getRiskMalattia() {
        return riskMalattia;
    }

    public void setRiskMalattia(String riskMalattia) {
        this.riskMalattia = riskMalattia;
    }

    public String getConsigli() {
        return consigli;
    }

    public void setConsigli(String consigli) {
        this.consigli = consigli;
    }
}