package rdf;
import exception.GrowthStage;
import exception.RiskLevel;
import model.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 * RdfService gestisce:
 * - trasformazione oggetti Java → RDF
 * - relazioni tra entità
 * - ontologia (RDFS)
 * - inferenze
 */
public class RdfService {

    private static final String NS = "http://orto.example/";

    private final Model model;

    // ===============================
    // PROPRIETA'
    // ===============================
    private final Property STORE_GDD;
    private final Property GROWTH_STAGE;
    private final Property TEMP_MAX;
    private final Property TEMP_MIN;
    private final Property UMIDITA;
    private final Property PRECIPITAZIONE;

    private final Property HAS_RISK;
    private final Property HAS_FORECAST;
    private final Property INFLUENCED_BY;
    private final Property GENERATES_RISK;
    private final Property IS_IN_DANGER;

    private final Property RISK_CALDO;
    private final Property RISK_FREDDO;
    private final Property RISK_VENTO;
    private final Property RISK_MALATTIA;
    private final Property CONSIGLI;

    // ===============================
    // CLASSI BASE (riusate ovunque)
    // ===============================
    private final Resource PLANT_INSTANCE_CLASS;
    private final Resource WEATHER_CLASS;
    private final Resource RISK_ASSESSMENT_CLASS;
    private final Resource FORECAST_CLASS;

    // ===============================
    // CLASSI SEMANTICHE
    // ===============================
    private final Resource CRITICAL_RISK;
    private final Resource MODERATE_RISK;
    private final Resource LOW_RISK;

    private final Resource EARLY_STAGE;
    private final Resource ADVANCED_STAGE;

    // ===============================
    // ONTOLOGIA (CLASSI)
    // ===============================
    private final Resource RISK_CLASS;
    private final Resource PLANT_CLASS;
    private final Resource GROWTH_STAGE_CLASS;

    public RdfService() {

        Model baseModel = ModelFactory.createDefaultModel();

        // Reasoner RDF Schema (usa subClassOf automaticamente)
        Reasoner reasoner = org.apache.jena.reasoner.ReasonerRegistry.getRDFSReasoner();

        // modello “intelligente”
        this.model = ModelFactory.createInfModel(reasoner, baseModel);

        model.setNsPrefix("orto", NS);

        // ===============================
        // PROPRIETA'
        // ===============================
        STORE_GDD = model.createProperty(NS, "storeGDD");
        GROWTH_STAGE = model.createProperty(NS, "growthStage");

        TEMP_MAX = model.createProperty(NS, "tempMax");
        TEMP_MIN = model.createProperty(NS, "tempMin");
        UMIDITA = model.createProperty(NS, "umidita");
        PRECIPITAZIONE = model.createProperty(NS, "precipitazione");

        HAS_RISK = model.createProperty(NS, "hasRisk");
        HAS_FORECAST = model.createProperty(NS, "hasForecast");
        INFLUENCED_BY = model.createProperty(NS, "influencedBy");
        GENERATES_RISK = model.createProperty(NS, "generatesRisk");
        IS_IN_DANGER = model.createProperty(NS, "isInDanger");

        RISK_CALDO = model.createProperty(NS, "riskCaldo");
        RISK_FREDDO = model.createProperty(NS, "riskFreddo");
        RISK_VENTO = model.createProperty(NS, "riskVento");
        RISK_MALATTIA = model.createProperty(NS, "riskMalattia");
        CONSIGLI = model.createProperty(NS, "consigli");

        // ===============================
        // CLASSI BASE
        // ===============================
        PLANT_INSTANCE_CLASS = model.createResource(NS + "PlantInstance");
        WEATHER_CLASS = model.createResource(NS + "WeatherDay");
        RISK_ASSESSMENT_CLASS = model.createResource(NS + "RiskAssessment");
        FORECAST_CLASS = model.createResource(NS + "GrowthForecast");

        // ===============================
        // CLASSI SEMANTICHE
        // ===============================
        CRITICAL_RISK = model.createResource(NS + "CriticalRisk");
        MODERATE_RISK = model.createResource(NS + "ModerateRisk");
        LOW_RISK = model.createResource(NS + "LowRisk");

        EARLY_STAGE = model.createResource(NS + "EarlyGrowthStage");
        ADVANCED_STAGE = model.createResource(NS + "AdvancedGrowthStage");

        // ===============================
        // ONTOLOGIA
        // ===============================
        RISK_CLASS = model.createResource(NS + "Risk");
        PLANT_CLASS = model.createResource(NS + "Plant");
        GROWTH_STAGE_CLASS = model.createResource(NS + "GrowthStage");

        // gerarchie
        CRITICAL_RISK.addProperty(RDFS.subClassOf, RISK_CLASS);
        MODERATE_RISK.addProperty(RDFS.subClassOf, RISK_CLASS);
        LOW_RISK.addProperty(RDFS.subClassOf, RISK_CLASS);

        ADVANCED_STAGE.addProperty(RDFS.subClassOf, GROWTH_STAGE_CLASS);
        EARLY_STAGE.addProperty(RDFS.subClassOf, GROWTH_STAGE_CLASS);

        // dominio / range
        HAS_RISK.addProperty(RDFS.domain, PLANT_CLASS);
        HAS_RISK.addProperty(RDFS.range, RISK_CLASS);

        HAS_FORECAST.addProperty(RDFS.domain, PLANT_CLASS);
        HAS_FORECAST.addProperty(RDFS.range, FORECAST_CLASS);

        GENERATES_RISK.addProperty(RDFS.range, RISK_CLASS);
        INFLUENCED_BY.addProperty(RDFS.domain, PLANT_CLASS);
    }

    public Model getModel() {
        return model;
    }

    // ===============================
    // EXPORT PLANT
    // ===============================

    public void exportPlantInstance(PlantInstance pianta) {
        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        Resource plant = model.createResource(NS + "plant/" + pianta.getPlantId());

        plant.addProperty(RDF.type, PLANT_INSTANCE_CLASS);
        plant.addProperty(RDF.type, PLANT_CLASS);

        if (pianta.getStoreGDD() != null) {
            plant.addLiteral(STORE_GDD, pianta.getStoreGDD());
        }

        if (pianta.getGrowthStage() != null) {
            GrowthStage stage = pianta.getGrowthStage();
            plant.addProperty(GROWTH_STAGE, stage.name());

            if (isAdvancedStage(stage)) {
                plant.addProperty(RDF.type, ADVANCED_STAGE);
            } else if (isEarlyStage(stage)) {
                plant.addProperty(RDF.type, EARLY_STAGE);
            }
        }
    }

    // ===============================
    // EXPORT WEATHER
    // ===============================
    public void exportWeatherDay(WeatherDay wd) {

        Resource weather = model.createResource(NS + "weather/" + wd.getWeatherId());
        weather.addProperty(RDF.type, WEATHER_CLASS);

        if (wd.getTempMax() != null) weather.addLiteral(TEMP_MAX, wd.getTempMax());
        if (wd.getTempMin() != null) weather.addLiteral(TEMP_MIN, wd.getTempMin());
        if (wd.getUmidita() != null) weather.addLiteral(UMIDITA, wd.getUmidita());
        if (wd.getPrecipitazione() != null) weather.addLiteral(PRECIPITAZIONE, wd.getPrecipitazione());
    }

    // ===============================
    // EXPORT RISK
    // ===============================
    public void exportRiskAssessment(RiskAssessment risk) {

        if (risk == null) {
            throw new IllegalArgumentException("RiskAssessment non può essere null");
        }

        Resource riskRes = model.createResource(NS + "risk/" + risk.getRiskId());

        riskRes.addProperty(RDF.type, RISK_ASSESSMENT_CLASS);
        riskRes.addProperty(RDF.type, RISK_CLASS);

        if (risk.getRiskCaldo() != null) {
            riskRes.addProperty(RISK_CALDO, risk.getRiskCaldo().name());
        }

        if (risk.getRiskFreddo() != null) {
            riskRes.addProperty(RISK_FREDDO, risk.getRiskFreddo().name());
        }

        if (risk.getRiskVento() != null) {
            riskRes.addProperty(RISK_VENTO, risk.getRiskVento().name());
        }

        if (risk.getRiskMalattia() != null) {
            riskRes.addProperty(RISK_MALATTIA, risk.getRiskMalattia().name());
        }

        if (risk.getConsigli() != null) {
            riskRes.addProperty(CONSIGLI, risk.getConsigli());
        }

        boolean isHigh =
                risk.getRiskCaldo() == RiskLevel.HIGH ||
                        risk.getRiskFreddo() == RiskLevel.HIGH ||
                        risk.getRiskVento() == RiskLevel.HIGH ||
                        risk.getRiskMalattia() == RiskLevel.HIGH;

        boolean isMedium =
                risk.getRiskCaldo() == RiskLevel.MEDIUM ||
                        risk.getRiskFreddo() == RiskLevel.MEDIUM ||
                        risk.getRiskVento() == RiskLevel.MEDIUM ||
                        risk.getRiskMalattia() == RiskLevel.MEDIUM;

        if (isHigh) {
            riskRes.addProperty(RDF.type, CRITICAL_RISK);
        } else if (isMedium) {
            riskRes.addProperty(RDF.type, MODERATE_RISK);
        } else {
            riskRes.addProperty(RDF.type, LOW_RISK);
        }
    }

    // ===============================
    // EXPORT FORECAST
    // ===============================
    public void exportGrowthForecast(GrowthForecast forecast) {

        Resource forecastRes = model.createResource(NS + "forecast/" + forecast.getForecastId());
        forecastRes.addProperty(RDF.type, FORECAST_CLASS);

        if (forecast.getPercentCiclo() != null) {
            forecastRes.addLiteral(model.createProperty(NS, "percentCiclo"), forecast.getPercentCiclo());
        }
    }

    // ===============================
    // RELAZIONI
    // ===============================
    public void collegaRelazioni(PlantInstance pianta, WeatherDay wd, RiskAssessment risk) {

        Resource plant = model.createResource(NS + "plant/" + pianta.getPlantId());
        Resource weather = model.createResource(NS + "weather/" + wd.getWeatherId());
        Resource riskRes = model.createResource(NS + "risk/" + risk.getRiskId());

        plant.addProperty(INFLUENCED_BY, weather);
        weather.addProperty(GENERATES_RISK, riskRes);
    }

    public void collegaPlantRisk(PlantInstance pianta, RiskAssessment risk) {
        model.createResource(NS + "plant/" + pianta.getPlantId())
                .addProperty(HAS_RISK, model.createResource(NS + "risk/" + risk.getRiskId()));
    }

    public void collegaPlantForecast(PlantInstance pianta, GrowthForecast forecast) {
        model.createResource(NS + "plant/" + pianta.getPlantId())
                .addProperty(HAS_FORECAST, model.createResource(NS + "forecast/" + forecast.getForecastId()));
    }

    // ===============================
    // INFERENZA
    // ===============================
    public void applicaInferenzaPiantePericolose() {

        String query = """
        PREFIX orto: <http://orto.example/>
        CONSTRUCT {
            ?plant orto:isInDanger true .
        }
        WHERE {
            ?plant orto:hasRisk ?risk .
            ?risk a orto:CriticalRisk .
        }
        """;

        Model inferito = QueryExecutionFactory.create(QueryFactory.create(query), model).execConstruct();
        model.add(inferito);
    }


    /**
     * Salva il modello RDF su file in formato Turtle (.ttl)
     */
    public void salvaRDFSuFile(String filename) {

        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(filename)) {

            model.write(fos, "TURTLE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    public void printRDF() {
        model.write(System.out, "TURTLE");
    }


    private boolean isAdvancedStage(GrowthStage stage) {
        return stage == GrowthStage.FIORITURA
                || stage == GrowthStage.FRUTTIFICAZIONE
                || stage == GrowthStage.MATURAZIONE;
    }

    private boolean isEarlyStage(GrowthStage stage) {
        return stage == GrowthStage.SEMINA
                || stage == GrowthStage.EMERGENZA
                || stage == GrowthStage.VEGETATIVO;
    }
}