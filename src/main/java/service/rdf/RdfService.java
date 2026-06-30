package service.rdf;


import static model.rdf.RdfVocabulary.*;

import model.data.*;
import model.rdf.RdfOntologyInitializer;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;


import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RdfService {

    private static final String RDF_FILE = "data/orto.ttl";
    private final RdfExportService rdfExportService;
    private final InfModel model;

    public RdfService(RdfOntologyInitializer rdfOntologyInitializer,
                      RdfExportService rdfExportService
                        ) {

        this.rdfExportService = rdfExportService;

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
        rdfExportService.exportGarden(model, location);
    }

    // ===============================
    // EXPORT PLANT
    // ===============================
    public void exportPlantInstance(PlantInstance pianta) {
        rdfExportService.exportPlantInstance(model, pianta);
    }

    // ===============================
    // EXPORT WEATHER
    // ===============================
    public void exportWeatherDay(WeatherDay wd) {
        rdfExportService.exportWeatherDay(model, wd);
    }

    // ===============================
    // EXPORT RISK
    // ===============================
    public void exportRiskAssessment(RiskAssessment risk) {
        rdfExportService.exportRiskAssessment(model, risk);
    }

    // ===============================
    // EXPORT FORECAST
    // ===============================
    public void exportGrowthForecast(GrowthForecast forecast) {
        rdfExportService.exportGrowthForecast(model, forecast);
    }

    // ===============================
    // EXPORT SIMULATION RUN
    // ===============================
    public String exportSimulationRun(Location location, String mode) {
        return rdfExportService.exportSimulationRun(model, location, mode);
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

    public void collegaForecastWeather(
            GrowthForecast forecast,
            WeatherDay weatherDay
    ) {
        if (forecast == null) {
            throw new IllegalArgumentException("GrowthForecast non può essere null");
        }

        if (forecast.getForecastId() == null) {
            throw new IllegalArgumentException("GrowthForecast deve avere un id");
        }

        if (weatherDay == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }

        if (weatherDay.getWeatherId() == null) {
            throw new IllegalArgumentException("WeatherDay deve avere un id");
        }

        Resource forecastRes =
                model.createResource(NS + "forecast/" + forecast.getForecastId());

        Resource weatherRes =
                model.createResource(NS + "weather/" + weatherDay.getWeatherId());

        forecastRes.addProperty(BASED_ON_WEATHER, weatherRes);
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
