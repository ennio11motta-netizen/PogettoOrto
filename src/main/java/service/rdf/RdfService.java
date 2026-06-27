package service.rdf;
import exception.GrowthStage;
import exception.RiskLevel;

import static model.rdf.RdfVocabulary.*;

import model.data.*;
import model.rdf.RdfOntologyInitializer;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.vocabulary.RDF;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * RdfService gestisce:
 * - trasformazione oggetti Java → RDF
 * - relazioni tra entità
 * - ontologia (RDFS)
 * - inferenze
 */

///////////////////////////////////////////////////////////////



@Service
public class RdfService {

    private static final String RDF_FILE = "data/orto.ttl";

    private final InfModel model;

    public RdfService(RdfOntologyInitializer rdfOntologyInitializer) {
        Model baseModel = ModelFactory.createDefaultModel();

        Reasoner reasoner =
                org.apache.jena.reasoner.ReasonerRegistry.getRDFSReasoner();

        this.model = ModelFactory.createInfModel(reasoner, baseModel);
        this.model.setNsPrefix("orto", NS);

        rdfOntologyInitializer.initialize(this.model);

        caricaRDFDaFile(RDF_FILE);
    }

    public Model getModel() {
        return model;
    }

    public void persist() {
        salvaRDFSuFile(RDF_FILE);
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

        Resource garden =
                model.createResource(NS + "garden/" + location.getLocationId());

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

        Resource plant =
                model.createResource(NS + "plant/" + pianta.getPlantId());

        removePlantMutableTriples(plant);

        plant.addProperty(RDF.type, PLANT_INSTANCE_CLASS);
        plant.addProperty(RDF.type, PLANT_CLASS);

        if (pianta.getNome() != null) {
            plant.addProperty(NAME, pianta.getNome());
        }

        if (pianta.getNote() != null) {
            plant.addProperty(NOTE, pianta.getNote());
        }

        if (
                pianta.getPlantSpecie() != null &&
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

    private void removePlantMutableTriples(Resource plant) {
        model.removeAll(plant, NAME, null);
        model.removeAll(plant, NOTE, null);
        model.removeAll(plant, SPECIES_NAME, null);
        model.removeAll(plant, STORE_GDD, null);
        model.removeAll(plant, GROWTH_STAGE, null);

        model.removeAll(plant, RDF.type, EARLY_STAGE);
        model.removeAll(plant, RDF.type, ADVANCED_STAGE);
    }

    // ===============================
    // EXPORT WEATHER
    // ===============================

    public void exportWeatherDay(WeatherDay wd) {
        if (wd == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }

        if (wd.getWeatherId() == null) {
            throw new IllegalArgumentException("WeatherDay deve avere un id");
        }

        Resource weather =
                model.createResource(NS + "weather/" + wd.getWeatherId());

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

        if (risk.getRiskId() == null) {
            throw new IllegalArgumentException("RiskAssessment deve avere un id");
        }

        Resource riskRes =
                model.createResource(NS + "risk/" + risk.getRiskId());

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

        if (forecast.getForecastId() == null) {
            throw new IllegalArgumentException("GrowthForecast deve avere un id");
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
            forecastRes.addLiteral(PERCENT_CICLO, forecast.getPercentCiclo());
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

    // ===============================
    // EXPORT SIMULATION RUN
    // ===============================

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

        Resource simulation =
                model.createResource(NS + "simulation/" + runId);

        Resource garden =
                model.createResource(NS + "garden/" + location.getLocationId());

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

    public void collegaGardenPlant(Location location, PlantInstance plant) {
        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (plant == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        Resource gardenRes =
                model.createResource(NS + "garden/" + location.getLocationId());

        Resource plantRes =
                model.createResource(NS + "plant/" + plant.getPlantId());

        gardenRes.addProperty(HAS_PLANT, plantRes);
        plantRes.addProperty(BELONGS_TO_GARDEN, gardenRes);
    }

    public void collegaRelazioni(
            PlantInstance pianta,
            WeatherDay wd,
            RiskAssessment risk
    ) {
        Resource plant =
                model.createResource(NS + "plant/" + pianta.getPlantId());

        Resource weather =
                model.createResource(NS + "weather/" + wd.getWeatherId());

        Resource riskRes =
                model.createResource(NS + "risk/" + risk.getRiskId());

        plant.addProperty(INFLUENCED_BY, weather);
        weather.addProperty(GENERATES_RISK, riskRes);
    }

    public void collegaPlantRisk(PlantInstance pianta, RiskAssessment risk) {
        model.createResource(NS + "plant/" + pianta.getPlantId())
                .addProperty(
                        HAS_RISK,
                        model.createResource(NS + "risk/" + risk.getRiskId())
                );
    }

    public void collegaPlantForecast(PlantInstance pianta, GrowthForecast forecast) {
        model.createResource(NS + "plant/" + pianta.getPlantId())
                .addProperty(
                        HAS_FORECAST,
                        model.createResource(NS + "forecast/" + forecast.getForecastId())
                );
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
            Resource plantRes =
                    model.createResource(NS + "plant/" + plant.getPlantId());

            simulation.addProperty(SIMULATION_HAS_PLANT, plantRes);
        }

        if (weatherDay != null && weatherDay.getWeatherId() != null) {
            Resource weatherRes =
                    model.createResource(NS + "weather/" + weatherDay.getWeatherId());

            simulation.addProperty(SIMULATION_HAS_WEATHER, weatherRes);
        }

        if (risk != null && risk.getRiskId() != null) {
            Resource riskRes =
                    model.createResource(NS + "risk/" + risk.getRiskId());

            simulation.addProperty(SIMULATION_HAS_RISK, riskRes);
        }

        if (forecast != null && forecast.getForecastId() != null) {
            Resource forecastRes =
                    model.createResource(NS + "forecast/" + forecast.getForecastId());

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

        Model inferito =
                QueryExecutionFactory
                        .create(QueryFactory.create(query), model)
                        .execConstruct();

        model.add(inferito);
    }

    // ===============================
    // FILE RDF
    // ===============================

    public void salvaRDFSuFile(String filename) {
        File file = new File(filename);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
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

    public void printRDF() {
        model.write(System.out, "TURTLE");
    }


    // ===============================
    // RESET RDF SIMULATION
    // ===============================
    public void resetSimulationDataForGarden(
            Location location,
            List<PlantInstance> plants
    ) {
        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (location.getLocationId() == null) {
            throw new IllegalArgumentException("Location deve avere un id");
        }

        if (plants == null) {
            throw new IllegalArgumentException("Lista piante non può essere null");
        }

        Resource garden =
                model.createResource(NS + "garden/" + location.getLocationId());

        Set<Resource> simulations = findSimulationRunsForGarden(garden);

        Set<Resource> weatherResources = new HashSet<>();
        Set<Resource> riskResources = new HashSet<>();
        Set<Resource> forecastResources = new HashSet<>();

        for (Resource simulation : simulations) {
            weatherResources.addAll(
                    listResourceObjects(simulation, SIMULATION_HAS_WEATHER)
            );

            riskResources.addAll(
                    listResourceObjects(simulation, SIMULATION_HAS_RISK)
            );

            forecastResources.addAll(
                    listResourceObjects(simulation, SIMULATION_HAS_FORECAST)
            );
        }

        for (Resource simulation : simulations) {
            removeResourceCompletely(simulation);
        }

        for (Resource weather : weatherResources) {
            removeResourceCompletely(weather);
        }

        for (Resource risk : riskResources) {
            removeResourceCompletely(risk);
        }

        for (Resource forecast : forecastResources) {
            removeResourceCompletely(forecast);
        }

        for (PlantInstance plant : plants) {
            if (plant == null || plant.getPlantId() == null) {
                continue;
            }

            Resource plantRes =
                    model.createResource(NS + "plant/" + plant.getPlantId());

            model.removeAll(plantRes, HAS_RISK, null);
            model.removeAll(plantRes, HAS_FORECAST, null);
            model.removeAll(plantRes, INFLUENCED_BY, null);
            model.removeAll(plantRes, IS_IN_DANGER, null);

            exportPlantInstance(plant);
            collegaGardenPlant(location, plant);
        }

        exportGarden(location);
    }


        // ===============================
    // UTILITY
    // ===============================

    private boolean isAdvancedStage(GrowthStage stage) {
        return stage == GrowthStage.FIORITURA ||
                stage == GrowthStage.FRUTTIFICAZIONE ||
                stage == GrowthStage.MATURAZIONE;
    }

    private boolean isEarlyStage(GrowthStage stage) {
        return stage == GrowthStage.SEMINA ||
                stage == GrowthStage.EMERGENZA ||
                stage == GrowthStage.VEGETATIVO;
    }

    private Set<Resource> findSimulationRunsForGarden(Resource garden) {
        Set<Resource> simulations = new HashSet<>();

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT DISTINCT ?simulation
            WHERE {
                ?simulation a orto:SimulationRun ;
                            orto:forGarden <%s> .
            }
            """.formatted(garden.getURI());

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();
                RDFNode simulationNode = solution.get("simulation");

                if (simulationNode != null && simulationNode.isResource()) {
                    simulations.add(simulationNode.asResource());
                }
            }
        }

        return simulations;
    }

    private Set<Resource> listResourceObjects(
            Resource subject,
            Property property
    ) {
        Set<Resource> resources = new HashSet<>();

        NodeIterator iterator =
                model.listObjectsOfProperty(subject, property);

        while (iterator.hasNext()) {
            RDFNode node = iterator.nextNode();

            if (node != null && node.isResource()) {
                resources.add(node.asResource());
            }
        }

        return resources;
    }

    private void removeResourceCompletely(Resource resource) {
        if (resource == null) {
            return;
        }

        model.removeAll(resource, null, null);
        model.removeAll(null, null, resource);
    }
}
