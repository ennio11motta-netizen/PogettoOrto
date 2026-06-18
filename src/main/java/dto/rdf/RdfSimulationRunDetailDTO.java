package dto.rdf;
import java.util.List;

    public class RdfSimulationRunDetailDTO {

        private String runId;
        private String simulationUri;
        private String mode;
        private String createdAt;
        private String gardenUri;

        private List<String> plantUris;
        private List<String> weatherUris;
        private List<String> riskUris;
        private List<String> forecastUris;

        public RdfSimulationRunDetailDTO() {
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

        public List<String> getPlantUris() {
            return plantUris;
        }

        public void setPlantUris(List<String> plantUris) {
            this.plantUris = plantUris;
        }

        public List<String> getWeatherUris() {
            return weatherUris;
        }

        public void setWeatherUris(List<String> weatherUris) {
            this.weatherUris = weatherUris;
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
    }

