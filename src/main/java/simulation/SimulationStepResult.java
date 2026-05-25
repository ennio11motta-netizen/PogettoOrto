package simulation;



import model.GrowthForecast;
import model.RiskAssessment;
import model.WeatherDay;

public class SimulationStepResult {

    private WeatherDay weatherDay;
    private GrowthForecast growthForecast;
    private RiskAssessment riskAssessment;

    public SimulationStepResult() {
    }

    public SimulationStepResult(
            WeatherDay weatherDay,
            GrowthForecast growthForecast,
            RiskAssessment riskAssessment
    ) {
        this.weatherDay = weatherDay;
        this.growthForecast = growthForecast;
        this.riskAssessment = riskAssessment;
    }

    public WeatherDay getWeatherDay() {
        return weatherDay;
    }

    public void setWeatherDay(WeatherDay weatherDay) {
        this.weatherDay = weatherDay;
    }

    public GrowthForecast getGrowthForecast() {
        return growthForecast;
    }

    public void setGrowthForecast(GrowthForecast growthForecast) {
        this.growthForecast = growthForecast;
    }

    public RiskAssessment getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(RiskAssessment riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    @Override
    public String toString() {
        return "SimulationStepResult{" +
                "weatherDay=" + weatherDay +
                ", growthForecast=" + growthForecast +
                ", riskAssessment=" + riskAssessment +
                '}';
    }
}
