package model.rdf;



import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public final class RdfVocabulary {

    private RdfVocabulary() {
        // classe di sole costanti RDF
    }

    public static final String NS = "http://orto.example/";
    public static final String RDF_FILE = "data/orto.ttl";

    // ===============================
    // PROPRIETÀ DATI BASE
    // ===============================

    public static final Property STORE_GDD =
            ResourceFactory.createProperty(NS, "storeGDD");

    public static final Property GROWTH_STAGE =
            ResourceFactory.createProperty(NS, "growthStage");

    public static final Property TEMP_MAX =
            ResourceFactory.createProperty(NS, "tempMax");

    public static final Property TEMP_MIN =
            ResourceFactory.createProperty(NS, "tempMin");

    public static final Property UMIDITA =
            ResourceFactory.createProperty(NS, "umidita");

    public static final Property PRECIPITAZIONE =
            ResourceFactory.createProperty(NS, "precipitazione");

    public static final Property DATE_TIME =
            ResourceFactory.createProperty(NS, "dateTime");

    public static final Property WIND_KMH =
            ResourceFactory.createProperty(NS, "windKmh");

    public static final Property UV_INDEX =
            ResourceFactory.createProperty(NS, "uvIndex");

    public static final Property GDD_DAILY =
            ResourceFactory.createProperty(NS, "gddDaily");

    public static final Property PERCENT_CICLO =
            ResourceFactory.createProperty(NS, "percentCiclo");

    public static final Property DAYS_TO_MATURITY =
            ResourceFactory.createProperty(NS, "daysToMaturity");


    public static final Property BASED_ON_WEATHER =
            ResourceFactory.createProperty(NS, "basedOnWeather");


    public static final Property NOTE =
            ResourceFactory.createProperty(NS, "note");

    public static final Property SPECIES_NAME =
            ResourceFactory.createProperty(NS, "speciesName");

    public static final Property NAME =
            ResourceFactory.createProperty(NS, "name");

    public static final Property LATITUDE =
            ResourceFactory.createProperty(NS, "latitude");

    public static final Property LONGITUDE =
            ResourceFactory.createProperty(NS, "longitude");

    // ===============================
    // RELAZIONI
    // ===============================

    public static final Property HAS_PLANT =
            ResourceFactory.createProperty(NS, "hasPlant");

    public static final Property BELONGS_TO_GARDEN =
            ResourceFactory.createProperty(NS, "belongsToGarden");

    public static final Property HAS_RISK =
            ResourceFactory.createProperty(NS, "hasRisk");

    public static final Property HAS_FORECAST =
            ResourceFactory.createProperty(NS, "hasForecast");

    public static final Property INFLUENCED_BY =
            ResourceFactory.createProperty(NS, "influencedBy");

    public static final Property GENERATES_RISK =
            ResourceFactory.createProperty(NS, "generatesRisk");

    public static final Property IS_IN_DANGER =
            ResourceFactory.createProperty(NS, "isInDanger");

    // ===============================
    // RISCHI
    // ===============================

    public static final Property RISK_CALDO =
            ResourceFactory.createProperty(NS, "riskCaldo");

    public static final Property RISK_FREDDO =
            ResourceFactory.createProperty(NS, "riskFreddo");

    public static final Property RISK_VENTO =
            ResourceFactory.createProperty(NS, "riskVento");

    public static final Property RISK_MALATTIA =
            ResourceFactory.createProperty(NS, "riskMalattia");

    public static final Property CONSIGLI =
            ResourceFactory.createProperty(NS, "consigli");

    // ===============================
    // SIMULATION RUN
    // ===============================

    public static final Property MODE =
            ResourceFactory.createProperty(NS, "mode");

    public static final Property CREATED_AT =
            ResourceFactory.createProperty(NS, "createdAt");

    public static final Property FOR_GARDEN =
            ResourceFactory.createProperty(NS, "forGarden");

    public static final Property SIMULATION_HAS_PLANT =
            ResourceFactory.createProperty(NS, "simulationHasPlant");

    public static final Property SIMULATION_HAS_WEATHER =
            ResourceFactory.createProperty(NS, "simulationHasWeather");

    public static final Property SIMULATION_HAS_RISK =
            ResourceFactory.createProperty(NS, "simulationHasRisk");

    public static final Property SIMULATION_HAS_FORECAST =
            ResourceFactory.createProperty(NS, "simulationHasForecast");

    // ===============================
    // CLASSI RDF
    // ===============================

    public static final Resource PLANT_INSTANCE_CLASS =
            ResourceFactory.createResource(NS + "PlantInstance");

    public static final Resource WEATHER_CLASS =
            ResourceFactory.createResource(NS + "WeatherDay");

    public static final Resource RISK_ASSESSMENT_CLASS =
            ResourceFactory.createResource(NS + "RiskAssessment");

    public static final Resource FORECAST_CLASS =
            ResourceFactory.createResource(NS + "GrowthForecast");

    public static final Resource GARDEN_CLASS =
            ResourceFactory.createResource(NS + "Garden");

    public static final Resource SIMULATION_RUN_CLASS =
            ResourceFactory.createResource(NS + "SimulationRun");

    public static final Resource PREVIEW_SIMULATION_CLASS =
            ResourceFactory.createResource(NS + "PreviewSimulation");

    public static final Resource APPLIED_SIMULATION_CLASS =
            ResourceFactory.createResource(NS + "AppliedSimulation");

    public static final Resource CRITICAL_RISK =
            ResourceFactory.createResource(NS + "CriticalRisk");

    public static final Resource MODERATE_RISK =
            ResourceFactory.createResource(NS + "ModerateRisk");

    public static final Resource LOW_RISK =
            ResourceFactory.createResource(NS + "LowRisk");

    public static final Resource EARLY_STAGE =
            ResourceFactory.createResource(NS + "EarlyGrowthStage");

    public static final Resource ADVANCED_STAGE =
            ResourceFactory.createResource(NS + "AdvancedGrowthStage");

    public static final Resource RISK_CLASS =
            ResourceFactory.createResource(NS + "Risk");

    public static final Resource PLANT_CLASS =
            ResourceFactory.createResource(NS + "Plant");

    public static final Resource GROWTH_STAGE_CLASS =
            ResourceFactory.createResource(NS + "GrowthStage");
}
