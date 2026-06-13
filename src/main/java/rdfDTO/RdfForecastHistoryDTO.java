package rdfDTO;



public class RdfForecastHistoryDTO {

    private String forecastUri;
    private String dateTime;
    private Double gddDaily;
    private Double percentCiclo;
    private String growthStage;
    private Integer daysToMaturity;

    public RdfForecastHistoryDTO() {
    }

    public String getForecastUri() {
        return forecastUri;
    }

    public void setForecastUri(String forecastUri) {
        this.forecastUri = forecastUri;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
}
