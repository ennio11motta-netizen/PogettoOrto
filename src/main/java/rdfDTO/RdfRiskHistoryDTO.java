package rdfDTO;

public class RdfRiskHistoryDTO {

    private String riskUri;
    private String riskCaldo;
    private String riskFreddo;
    private String riskVento;
    private String riskMalattia;
    private String consigli;

    private String plantUri;
    private String plantName;


    private String weatherUri;
    private String dateTime;



    public RdfRiskHistoryDTO() {
    }

    public String getRiskUri() {
        return riskUri;
    }

    public void setRiskUri(String riskUri) {
        this.riskUri = riskUri;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getWeatherUri() {
        return weatherUri;
    }

    public void setWeatherUri(String weatherUri) {
        this.weatherUri = weatherUri;
    }
}