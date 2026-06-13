package rdfDTO;


public class RdfWeatherRiskRelationDTO {

    private String weatherUri;
    private String riskUri;

    public RdfWeatherRiskRelationDTO() {
    }

    public RdfWeatherRiskRelationDTO(String weatherUri, String riskUri) {
        this.weatherUri = weatherUri;
        this.riskUri = riskUri;
    }

    public String getWeatherUri() {
        return weatherUri;
    }

    public void setWeatherUri(String weatherUri) {
        this.weatherUri = weatherUri;
    }

    public String getRiskUri() {
        return riskUri;
    }

    public void setRiskUri(String riskUri) {
        this.riskUri = riskUri;
    }
}
