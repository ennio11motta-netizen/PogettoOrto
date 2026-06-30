package dto.rdf;

public class RdfForecastHistoryDTO {

    private String forecastUri;
    private String forecastCreatedAt;
    private Double gddDaily;
    private Double percentCiclo;
    private String growthStage;
    private Integer daysToMaturity;
    private String plantUri;
    private String plantName;
    private String weatherUri;
    private String weatherDateTime;



    public RdfForecastHistoryDTO() {
    }

    public String getForecastUri() {
        return forecastUri;
    }

    public void setForecastUri(String forecastUri) {
        this.forecastUri = forecastUri;
    }

    public Double getGddDaily() {
        return gddDaily;
    }

    public void setGddDaily(Double gddDaily) {
        this.gddDaily = gddDaily;
    }

    public Double getPercentCiclo() {
        return percentCiclo;
    }

    public void setPercentCiclo(Double percentCiclo) {
        this.percentCiclo = percentCiclo;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public void setGrowthStage(String growthStage) {
        this.growthStage = growthStage;
    }

    public Integer getDaysToMaturity() {
        return daysToMaturity;
    }

    public void setDaysToMaturity(Integer daysToMaturity) {
        this.daysToMaturity = daysToMaturity;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantUri() {
        return plantUri;
    }

    public void setPlantUri(String plantUri) {
        this.plantUri = plantUri;
    }

    public String getWeatherDateTime() {
        return weatherDateTime;
    }

    public void setWeatherDateTime(String weatherDateTime) {
        this.weatherDateTime = weatherDateTime;
    }

    public String getWeatherUri() {
        return weatherUri;
    }

    public void setWeatherUri(String weatherUri) {
        this.weatherUri = weatherUri;
    }


    public String getForecastCreatedAt() {
        return forecastCreatedAt;
    }

    public void setForecastCreatedAt(String forecastCreatedAt) {
        this.forecastCreatedAt = forecastCreatedAt;
    }
}
