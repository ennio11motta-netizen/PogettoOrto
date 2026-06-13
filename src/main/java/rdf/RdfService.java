package rdf;
import exception.GrowthStage;
import exception.RiskLevel;
import model.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * RdfService gestisce:
 * - trasformazione oggetti Java → RDF
 * - relazioni tra entità
 * - ontologia (RDFS)
 * - inferenze
 */
@Service
public class RdfService {

    private static final String NS = "http://orto.example/";

    private final InfModel model;

    // ===============================
    // PROPRIETA'
    // ===============================
    private final Property STORE_GDD;
    private final Property GROWTH_STAGE;

    private final Property TEMP_MAX;
    private final Property TEMP_MIN;
    private final Property UMIDITA;
    private final Property PRECIPITAZIONE;


    private final Property NAME;
    private final Property LATITUDE;
    private final Property LONGITUDE;
    private final Property HAS_PLANT;
    private final Property BELONGS_TO_GARDEN;


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


    private final Property MODE;
    private final Property CREATED_AT;
    private final Property FOR_GARDEN;
    private final Property SIMULATION_HAS_PLANT;
    private final Property SIMULATION_HAS_WEATHER;
    private final Property SIMULATION_HAS_RISK;
    private final Property SIMULATION_HAS_FORECAST;


    private final Property DATE_TIME;
    private final Property WIND_KMH;
    private final Property UV_INDEX;
    private final Property GDD_DAILY;
    private final Property DAYS_TO_MATURITY;
    private final Property NOTE;
    private final Property SPECIES_NAME;


    // ===============================
    // CLASSI BASE (riusate ovunque)
    // ===============================
    private final Resource PLANT_INSTANCE_CLASS;
    private final Resource WEATHER_CLASS;
    private final Resource RISK_ASSESSMENT_CLASS;
    private final Resource FORECAST_CLASS;
    private final Resource GARDEN_CLASS;

    private final Resource SIMULATION_RUN_CLASS;
    private final Resource PREVIEW_SIMULATION_CLASS;
    private final Resource APPLIED_SIMULATION_CLASS;
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

    private static final String RDF_FILE = "data/orto.ttl";


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

        DATE_TIME = model.createProperty(NS, "dateTime");
        WIND_KMH = model.createProperty(NS, "windKmh");
        UV_INDEX = model.createProperty(NS, "uvIndex");
        GDD_DAILY = model.createProperty(NS, "gddDaily");
        DAYS_TO_MATURITY = model.createProperty(NS, "daysToMaturity");
        NOTE = model.createProperty(NS, "note");
        SPECIES_NAME = model.createProperty(NS, "speciesName");


        NAME = model.createProperty(NS, "name");
        LATITUDE = model.createProperty(NS, "latitude");
        LONGITUDE = model.createProperty(NS, "longitude");
        HAS_PLANT = model.createProperty(NS, "hasPlant");
        BELONGS_TO_GARDEN = model.createProperty(NS, "belongsToGarden");


        HAS_RISK = model.createProperty(NS, "hasRisk");
        HAS_FORECAST = model.createProperty(NS, "hasForecast");
        INFLUENCED_BY = model.createProperty(NS, "influencedBy");
        GENERATES_RISK = model.createProperty(NS, "generatesRisk");
        IS_IN_DANGER = model.createProperty(NS, "isInDanger");


        MODE = model.createProperty(NS, "mode");
        CREATED_AT = model.createProperty(NS, "createdAt");
        FOR_GARDEN = model.createProperty(NS, "forGarden");
        SIMULATION_HAS_PLANT = model.createProperty(NS, "simulationHasPlant");
        SIMULATION_HAS_WEATHER = model.createProperty(NS, "simulationHasWeather");
        SIMULATION_HAS_RISK = model.createProperty(NS, "simulationHasRisk");
        SIMULATION_HAS_FORECAST = model.createProperty(NS, "simulationHasForecast");


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
        GARDEN_CLASS = model.createResource(NS + "Garden");


        SIMULATION_RUN_CLASS = model.createResource(NS + "SimulationRun");
        PREVIEW_SIMULATION_CLASS = model.createResource(NS + "PreviewSimulation");
        APPLIED_SIMULATION_CLASS = model.createResource(NS + "AppliedSimulation");

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

        //================================
        //  LABELS
        //===============================

        NAME.addProperty(RDFS.label, model.createLiteral("nome", "it"));
        LATITUDE.addProperty(RDFS.label, model.createLiteral("latitudine", "it"));
        LONGITUDE.addProperty(RDFS.label, model.createLiteral("longitudine", "it"));
        HAS_PLANT.addProperty(RDFS.label, model.createLiteral("ha pianta", "it"));
        BELONGS_TO_GARDEN.addProperty(RDFS.label, model.createLiteral("appartiene all'orto", "it"));
        GARDEN_CLASS.addProperty(RDFS.label, model.createLiteral("orto", "it"));

        DATE_TIME.addProperty(RDFS.label, model.createLiteral("data e ora", "it"));
        WIND_KMH.addProperty(RDFS.label, model.createLiteral("vento km/h", "it"));
        UV_INDEX.addProperty(RDFS.label, model.createLiteral("indice UV", "it"));
        GDD_DAILY.addProperty(RDFS.label, model.createLiteral("GDD giornaliero", "it"));
        DAYS_TO_MATURITY.addProperty(RDFS.label, model.createLiteral("giorni alla maturazione", "it"));
        NOTE.addProperty(RDFS.label, model.createLiteral("note", "it"));
        SPECIES_NAME.addProperty(RDFS.label, model.createLiteral("nome specie", "it"));


        MODE.addProperty(RDFS.label, model.createLiteral("modalità", "it"));
        CREATED_AT.addProperty(RDFS.label, model.createLiteral("creata il", "it"));
        FOR_GARDEN.addProperty(RDFS.label, model.createLiteral("per orto", "it"));
        SIMULATION_HAS_PLANT.addProperty(RDFS.label, model.createLiteral("simulazione ha pianta", "it"));
        SIMULATION_HAS_WEATHER.addProperty(RDFS.label, model.createLiteral("simulazione ha meteo", "it"));
        SIMULATION_HAS_RISK.addProperty(RDFS.label, model.createLiteral("simulazione ha rischio", "it"));
        SIMULATION_HAS_FORECAST.addProperty(RDFS.label, model.createLiteral("simulazione ha previsione crescita", "it"));


        SIMULATION_RUN_CLASS.addProperty(RDFS.label, model.createLiteral("simulazione", "it"));
        PREVIEW_SIMULATION_CLASS.addProperty(RDFS.label, model.createLiteral("simulazione previsionale", "it"));
        APPLIED_SIMULATION_CLASS.addProperty(RDFS.label, model.createLiteral("simulazione applicata", "it"));


        //====================================
        //  GERARCHIE
        //====================================
        CRITICAL_RISK.addProperty(RDFS.subClassOf, RISK_CLASS);
        MODERATE_RISK.addProperty(RDFS.subClassOf, RISK_CLASS);
        LOW_RISK.addProperty(RDFS.subClassOf, RISK_CLASS);

        ADVANCED_STAGE.addProperty(RDFS.subClassOf, GROWTH_STAGE_CLASS);
        EARLY_STAGE.addProperty(RDFS.subClassOf, GROWTH_STAGE_CLASS);

        PREVIEW_SIMULATION_CLASS.addProperty(RDFS.subClassOf, SIMULATION_RUN_CLASS);
        APPLIED_SIMULATION_CLASS.addProperty(RDFS.subClassOf, SIMULATION_RUN_CLASS);

        //===================================
        // DOMINIO/RANGE
        //==================================
        HAS_RISK.addProperty(RDFS.domain, PLANT_CLASS);
        HAS_RISK.addProperty(RDFS.range, RISK_CLASS);

        HAS_FORECAST.addProperty(RDFS.domain, PLANT_CLASS);
        HAS_FORECAST.addProperty(RDFS.range, FORECAST_CLASS);

        GENERATES_RISK.addProperty(RDFS.range, RISK_CLASS);
        INFLUENCED_BY.addProperty(RDFS.domain, PLANT_CLASS);


        HAS_PLANT.addProperty(RDFS.domain, GARDEN_CLASS);
        HAS_PLANT.addProperty(RDFS.range, PLANT_CLASS);

        BELONGS_TO_GARDEN.addProperty(RDFS.domain, PLANT_CLASS);
        BELONGS_TO_GARDEN.addProperty(RDFS.range, GARDEN_CLASS);

        MODE.addProperty(RDFS.domain, SIMULATION_RUN_CLASS);
        MODE.addProperty(RDFS.range, RDFS.Literal);

        CREATED_AT.addProperty(RDFS.domain, SIMULATION_RUN_CLASS);
        CREATED_AT.addProperty(RDFS.range, RDFS.Literal);

        FOR_GARDEN.addProperty(RDFS.domain, SIMULATION_RUN_CLASS);
        FOR_GARDEN.addProperty(RDFS.range, GARDEN_CLASS);

        SIMULATION_HAS_PLANT.addProperty(RDFS.domain, SIMULATION_RUN_CLASS);
        SIMULATION_HAS_PLANT.addProperty(RDFS.range, PLANT_CLASS);

        SIMULATION_HAS_WEATHER.addProperty(RDFS.domain, SIMULATION_RUN_CLASS);
        SIMULATION_HAS_WEATHER.addProperty(RDFS.range, WEATHER_CLASS);

        SIMULATION_HAS_RISK.addProperty(RDFS.domain, SIMULATION_RUN_CLASS);
        SIMULATION_HAS_RISK.addProperty(RDFS.range, RISK_CLASS);

        SIMULATION_HAS_FORECAST.addProperty(RDFS.domain, SIMULATION_RUN_CLASS);
        SIMULATION_HAS_FORECAST.addProperty(RDFS.range, FORECAST_CLASS);


        //carica DOPO aver definito tutto
        caricaRDFDaFile(RDF_FILE);

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


        if (pianta.getPlantId() == null) {
            throw new IllegalArgumentException("PlantInstance deve avere un id");
        }


        Resource plant = model.createResource(NS + "plant/" + pianta.getPlantId());

        removePlantMutableTriples(plant);

        plant.addProperty(RDF.type, PLANT_INSTANCE_CLASS);
        plant.addProperty(RDF.type, PLANT_CLASS);


        if (pianta.getNome() != null) {
            plant.addProperty(NAME, pianta.getNome());
        }


        if (pianta.getNote() != null) {
            plant.addProperty(NOTE, pianta.getNote());
        }

        if (pianta.getPlantSpecie() != null &&
                        pianta.getPlantSpecie().getNome() != null
        ) {
            plant.addProperty(SPECIES_NAME, pianta.getPlantSpecie().getNome());
        }

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
        if (wd == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }

        Resource weather = model.createResource(NS + "weather/" + wd.getWeatherId());

        weather.addProperty(RDF.type, WEATHER_CLASS);

        if (wd.getData() != null) {
            weather.addProperty(DATE_TIME, wd.getData().toString());
        }

        if (wd.getTempMax() != null) {
            weather.addLiteral(TEMP_MAX, wd.getTempMax());
        }

        if (wd.getTempMin() != null) {
            weather.addLiteral(TEMP_MIN, wd.getTempMin());
        }

        if (wd.getUmidita() != null) {
            weather.addLiteral(UMIDITA, wd.getUmidita());
        }

        if (wd.getPrecipitazione() != null) {
            weather.addLiteral(PRECIPITAZIONE, wd.getPrecipitazione());
        }

        if (wd.getVentoKmh() != null) {
            weather.addLiteral(WIND_KMH, wd.getVentoKmh());
        }

        if (wd.getUvIndex() != null) {
            weather.addLiteral(UV_INDEX, wd.getUvIndex());
        }
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
        if (forecast == null) {
            throw new IllegalArgumentException("GrowthForecast non può essere null");
        }

        Resource forecastRes =
                model.createResource(NS + "forecast/" + forecast.getForecastId());

        forecastRes.addProperty(RDF.type, FORECAST_CLASS);

        if (forecast.getDateTime() != null) {
            forecastRes.addProperty(DATE_TIME, forecast.getDateTime().toString());
        }

        if (forecast.getGddPrevistiGiorn() != null) {
            forecastRes.addLiteral(GDD_DAILY, forecast.getGddPrevistiGiorn());
        }

        if (forecast.getPercentCiclo() != null) {
            forecastRes.addLiteral(
                    model.createProperty(NS, "percentCiclo"),
                    forecast.getPercentCiclo()
            );
        }

        if (forecast.getStadioPrevisto() != null) {
            forecastRes.addProperty(
                    GROWTH_STAGE,
                    forecast.getStadioPrevisto().name()
            );
        }

        if (forecast.getGiorniNuovoStadio() != null) {
            forecastRes.addLiteral(
                    DAYS_TO_MATURITY,
                    forecast.getGiorniNuovoStadio()
            );
        }
    }

    //===============================
    // EXPORT SIMULAZIONE
    //===============================

    public String exportSimulationRun(Location location, String mode) {
        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (location.getLocationId() == null) {
            throw new IllegalArgumentException("Location deve avere un id");
        }

        if (mode == null || mode.isBlank()) {
            throw new IllegalArgumentException("Mode simulazione obbligatoria");
        }

        String normalizedMode = mode.trim().toUpperCase();

        if (!normalizedMode.equals("PREVIEW") && !normalizedMode.equals("APPLIED")) {
            throw new IllegalArgumentException("Mode simulazione non valida: " + mode);
        }

        String runId = location.getLocationId() + "-" + System.currentTimeMillis();

        Resource simulation = model.createResource(NS + "simulation/" + runId);
        Resource garden = model.createResource(NS + "garden/" + location.getLocationId());

        simulation.addProperty(RDF.type, SIMULATION_RUN_CLASS);

        if (normalizedMode.equals("PREVIEW")) {
            simulation.addProperty(RDF.type, PREVIEW_SIMULATION_CLASS);
        }

        if (normalizedMode.equals("APPLIED")) {
            simulation.addProperty(RDF.type, APPLIED_SIMULATION_CLASS);
        }

        simulation.addProperty(MODE, normalizedMode);
        simulation.addProperty(CREATED_AT, LocalDateTime.now().toString());
        simulation.addProperty(FOR_GARDEN, garden);

        return simulation.getURI();
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

    public void collegaGardenPlant(Location location, PlantInstance plant) {
        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (plant == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (location.getLocationId() == null) {
            throw new IllegalArgumentException("Location deve avere un id");
        }

        if (plant.getPlantId() == null) {
            throw new IllegalArgumentException("PlantInstance deve avere un id");
        }

        Resource gardenRes = model.createResource(NS + "garden/" + location.getLocationId());
        Resource plantRes = model.createResource(NS + "plant/" + plant.getPlantId());

        gardenRes.addProperty(HAS_PLANT, plantRes);
        plantRes.addProperty(BELONGS_TO_GARDEN, gardenRes);
    }



    public void collegaSimulationStep(
            String simulationUri,
            PlantInstance plant,
            WeatherDay weatherDay,
            RiskAssessment risk,
            GrowthForecast forecast
    ) {
        if (simulationUri == null || simulationUri.isBlank()) {
            throw new IllegalArgumentException("simulationUri obbligatorio");
        }

        Resource simulation = model.createResource(simulationUri);

        if (plant != null && plant.getPlantId() != null) {
            Resource plantRes = model.createResource(NS + "plant/" + plant.getPlantId());
            simulation.addProperty(SIMULATION_HAS_PLANT, plantRes);
        }

        if (weatherDay != null && weatherDay.getWeatherId() != null) {
            Resource weatherRes = model.createResource(NS + "weather/" + weatherDay.getWeatherId());
            simulation.addProperty(SIMULATION_HAS_WEATHER, weatherRes);
        }

        if (risk != null && risk.getRiskId() != null) {
            Resource riskRes = model.createResource(NS + "risk/" + risk.getRiskId());
            simulation.addProperty(SIMULATION_HAS_RISK, riskRes);
        }

        if (forecast != null && forecast.getForecastId() != null) {
            Resource forecastRes = model.createResource(NS + "forecast/" + forecast.getForecastId());
            simulation.addProperty(SIMULATION_HAS_FORECAST, forecastRes);
        }
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
        File file= new File(filename);
        File parent= file.getParentFile();

        if(parent != null && !parent.exists()){
            parent.mkdirs();
        }
        try (java.io.FileOutputStream fos = new java.io.FileOutputStream(filename)) {

            model.getRawModel().write(fos, "TURTLE");
            System.out.println("RDF salvato correttamente ✅");


        } catch (Exception e) {
            throw new RuntimeException("Errore nel salvataggio RDF: " + filename, e);
        }
    }



    public void caricaRDFDaFile(String filename) {
        File file = new File(filename);

        if (!file.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            model.read(fis, null, "TURTLE");
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento RDF da file: " + filename, e);
        }
    }

    // ===============================
//   // public void printRDF() {
//        model.write(System.out, "TURTLE");
//    }


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




    // ===============================
// EXPORT GARDEN
// ===============================
    public void exportGarden(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (location.getLocationId() == null) {
            throw new IllegalArgumentException("Location deve avere un id");
        }

        Resource garden = model.createResource(NS + "garden/" + location.getLocationId());

        garden.addProperty(RDF.type, GARDEN_CLASS);

        if (location.getNome() != null) {
            garden.addProperty(NAME, location.getNome());
        }

        if (location.getLatitudine() != null) {
            garden.addLiteral(LATITUDE, location.getLatitudine());
        }

        if (location.getLongitudine() != null) {
            garden.addLiteral(LONGITUDE, location.getLongitudine());
        }
    }


    private void removePlantMutableTriples(Resource plant) {
        model.removeAll(plant, NAME, null);
        model.removeAll(plant, NOTE, null);
        model.removeAll(plant, SPECIES_NAME, null);
        model.removeAll(plant, STORE_GDD, null);
        model.removeAll(plant, GROWTH_STAGE, null);

        model.removeAll(plant, RDF.type, EARLY_STAGE);
        model.removeAll(plant, RDF.type, ADVANCED_STAGE);
    }
}