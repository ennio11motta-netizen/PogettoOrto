package dto.rdf;

import java.util.List;

public class RdfSimulationRunHistoryDTO {

    private String runId;
    private String simulationUri;
    private String mode;
    private String createdAt;
    private String gardenUri;
    private List<RdfPlantHistoryDTO> plants;
    private List<RdfWeatherHistoryDTO> weatherDays;
    private List<RdfRiskHistoryDTO> risks;
    private List<RdfForecastHistoryDTO> forecasts;

    public RdfSimulationRunHistoryDTO() {
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getSimulationUri() {
        return simulationUri;
    }

    public void setSimulationUri(String simulationUri) {
        this.simulationUri = simulationUri;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGardenUri() {
        return gardenUri;
    }

    public void setGardenUri(String gardenUri) {
        this.gardenUri = gardenUri;
    }

    public List<RdfPlantHistoryDTO> getPlants() {
        return plants;
    }

    public void setPlants(List<RdfPlantHistoryDTO> plants) {
        this.plants = plants;
    }

    public List<RdfWeatherHistoryDTO> getWeatherDays() {
        return weatherDays;
    }

    public void setWeatherDays(List<RdfWeatherHistoryDTO> weatherDays) {
        this.weatherDays = weatherDays;
    }

    public List<RdfRiskHistoryDTO> getRisks() {
        return risks;
    }

    public void setRisks(List<RdfRiskHistoryDTO> risks) {
        this.risks = risks;
    }

    public List<RdfForecastHistoryDTO> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<RdfForecastHistoryDTO> forecasts) {
        this.forecasts = forecasts;
    }
}

