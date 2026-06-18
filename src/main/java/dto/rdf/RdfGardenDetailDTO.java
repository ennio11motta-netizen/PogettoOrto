package dto.rdf;



import java.util.List;

public class RdfGardenDetailDTO {

    private Integer locationId;
    private String gardenUri;
    private String gardenName;
    private Double latitude;
    private Double longitude;

    private List<String> plantUris;
    private List<String> riskUris;
    private List<String> forecastUris;
    private List<String> dangerPlantUris;
    private List<RdfWeatherRiskRelationDTO> weatherRiskRelations;

    public RdfGardenDetailDTO() {
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getGardenUri() {
        return gardenUri;
    }

    public void setGardenUri(String gardenUri) {
        this.gardenUri = gardenUri;
    }

    public String getGardenName() {
        return gardenName;
    }

    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<String> getPlantUris() {
        return plantUris;
    }

    public void setPlantUris(List<String> plantUris) {
        this.plantUris = plantUris;
    }

    public List<String> getRiskUris() {
        return riskUris;
    }

    public void setRiskUris(List<String> riskUris) {
        this.riskUris = riskUris;
    }

    public List<String> getForecastUris() {
        return forecastUris;
    }

    public void setForecastUris(List<String> forecastUris) {
        this.forecastUris = forecastUris;
    }

    public List<String> getDangerPlantUris() {
        return dangerPlantUris;
    }

    public void setDangerPlantUris(List<String> dangerPlantUris) {
        this.dangerPlantUris = dangerPlantUris;
    }

    public List<RdfWeatherRiskRelationDTO> getWeatherRiskRelations() {
        return weatherRiskRelations;
    }

    public void setWeatherRiskRelations(List<RdfWeatherRiskRelationDTO> weatherRiskRelations) {
        this.weatherRiskRelations = weatherRiskRelations;
    }
}
