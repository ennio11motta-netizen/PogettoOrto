package dto.rdf;

public class RdfWeatherHistoryDTO {

    private String weatherUri;
    private String dateTime;
    private Double tempMin;
    private Double tempMax;
    private Double humidity;
    private Double precipitation;
    private Double windKmh;
    private Double uvIndex;

    public RdfWeatherHistoryDTO() {
    }

    public String getWeatherUri() {
        return weatherUri;
    }

    public void setWeatherUri(String weatherUri) {
        this.weatherUri = weatherUri;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Double getWindKmh() {
        return windKmh;
    }

    public void setWindKmh(Double windKmh) {
        this.windKmh = windKmh;
    }

    public Double getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(Double uvIndex) {
        this.uvIndex = uvIndex;
    }
}
