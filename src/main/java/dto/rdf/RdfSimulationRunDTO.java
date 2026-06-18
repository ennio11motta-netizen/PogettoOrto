package dto.rdf;

public class RdfSimulationRunDTO {


        private String simulationUri;
        private String mode;
        private String createdAt;
        private String gardenUri;

        private Integer plantCount;
        private Integer weatherCount;
        private Integer riskCount;
        private Integer forecastCount;

        public RdfSimulationRunDTO() {
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

        public Integer getPlantCount() {
            return plantCount;
        }

        public void setPlantCount(Integer plantCount) {
            this.plantCount = plantCount;
        }

        public Integer getWeatherCount() {
            return weatherCount;
        }

        public void setWeatherCount(Integer weatherCount) {
            this.weatherCount = weatherCount;
        }

        public Integer getRiskCount() {
            return riskCount;
        }

        public void setRiskCount(Integer riskCount) {
            this.riskCount = riskCount;
        }

        public Integer getForecastCount() {
            return forecastCount;
        }

        public void setForecastCount(Integer forecastCount) {
            this.forecastCount = forecastCount;
        }
    }

