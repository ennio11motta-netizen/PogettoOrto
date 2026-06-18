package service.rdf;


import dto.rdf.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RdfSimulationRunService {

    private static final String NS = "http://orto.example/";

    private final RdfService rdfService;

    public RdfSimulationRunService(RdfService rdfService) {
        this.rdfService = rdfService;
    }

    public List<RdfSimulationRunDTO> getSimulationRunsByGarden(Integer locationId) {
        if (locationId == null) {
            throw new IllegalArgumentException("locationId obbligatorio");
        }

        String gardenUri = NS + "garden/" + locationId;
        Model model = rdfService.getModel();

        String queryString = """
                PREFIX orto: <http://orto.example/>

                SELECT ?simulation ?mode ?createdAt
                       (COUNT(DISTINCT ?plant) AS ?plantCount)
                       (COUNT(DISTINCT ?weather) AS ?weatherCount)
                       (COUNT(DISTINCT ?risk) AS ?riskCount)
                       (COUNT(DISTINCT ?forecast) AS ?forecastCount)
                WHERE {
                    ?simulation a orto:SimulationRun ;
                                orto:forGarden <%s> .

                    OPTIONAL { ?simulation orto:mode ?mode . }
                    OPTIONAL { ?simulation orto:createdAt ?createdAt . }
                    OPTIONAL { ?simulation orto:simulationHasPlant ?plant . }
                    OPTIONAL { ?simulation orto:simulationHasWeather ?weather . }
                    OPTIONAL { ?simulation orto:simulationHasRisk ?risk . }
                    OPTIONAL { ?simulation orto:simulationHasForecast ?forecast . }
                }
                GROUP BY ?simulation ?mode ?createdAt
                ORDER BY DESC(?createdAt)
                """.formatted(gardenUri);

        Query query = QueryFactory.create(queryString);

        List<RdfSimulationRunDTO> response = new ArrayList<>();

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                RdfSimulationRunDTO dto = new RdfSimulationRunDTO();

                RDFNode simulationNode = solution.get("simulation");

                if (simulationNode != null) {
                    dto.setSimulationUri(simulationNode.toString());
                }

                dto.setGardenUri(gardenUri);
                dto.setMode(getLiteralString(solution, "mode"));
                dto.setCreatedAt(getLiteralString(solution, "createdAt"));

                dto.setPlantCount(getLiteralInteger(solution, "plantCount"));
                dto.setWeatherCount(getLiteralInteger(solution, "weatherCount"));
                dto.setRiskCount(getLiteralInteger(solution, "riskCount"));
                dto.setForecastCount(getLiteralInteger(solution, "forecastCount"));

                response.add(dto);
            }
        }

        return response;
    }
    ///////////////////////////////////////////
    public List<RdfSimulationRunHistoryDTO> getSimulationHistoryByGarden(Integer locationId) {
        if (locationId == null) {
            throw new IllegalArgumentException("locationId obbligatorio");
        }

        String gardenUri = NS + "garden/" + locationId;
        Model model = rdfService.getModel();

        List<String> simulationUris = querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>
    
                SELECT DISTINCT ?simulation
                WHERE {
                    ?simulation a orto:SimulationRun ;
                                orto:forGarden <%s> .
                }
                ORDER BY DESC(?simulation)
                """.formatted(gardenUri),
                "simulation"
        );

        List<RdfSimulationRunHistoryDTO> history = new ArrayList<>();

        for (String simulationUri : simulationUris) {
            RdfSimulationRunHistoryDTO dto = new RdfSimulationRunHistoryDTO();

            dto.setSimulationUri(simulationUri);
            dto.setRunId(extractRunId(simulationUri));

            loadHistoryMetadata(model, simulationUri, dto);

            dto.setPlants(queryPlantsForSimulation(model, simulationUri));
            dto.setWeatherDays(queryWeatherForSimulation(model, simulationUri));
            dto.setRisks(queryRisksForSimulation(model, simulationUri));
            dto.setForecasts(queryForecastsForSimulation(model, simulationUri));

            history.add(dto);
        }

        return history;
    }

    public RdfSimulationRunDetailDTO getSimulationRunDetail(String runId) {
        if (runId == null || runId.isBlank()) {
            throw new IllegalArgumentException("runId simulazione obbligatorio");
        }

        String simulationUri = NS + "simulation/" + runId;
        Model model = rdfService.getModel();

        if (!simulationRunExists(model, simulationUri)) {
            throw new IllegalArgumentException(
                    "SimulationRun non trovata nel grafo RDF: " + runId
            );
        }

        RdfSimulationRunDetailDTO dto = new RdfSimulationRunDetailDTO();
        dto.setRunId(runId);
        dto.setSimulationUri(simulationUri);

        loadSimulationMetadata(model, simulationUri, dto);

        dto.setPlantUris(querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>
    
                SELECT DISTINCT ?plant
                WHERE {
                    <%s> orto:simulationHasPlant ?plant .
                }
                """.formatted(simulationUri),
                "plant"
        ));

        dto.setWeatherUris(querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>
    
                SELECT DISTINCT ?weather
                WHERE {
                    <%s> orto:simulationHasWeather ?weather .
                }
                """.formatted(simulationUri),
                "weather"
        ));

        dto.setRiskUris(querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>
    
                SELECT DISTINCT ?risk
                WHERE {
                    <%s> orto:simulationHasRisk ?risk .
                }
                """.formatted(simulationUri),
                "risk"
        ));

        dto.setForecastUris(querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>
    
                SELECT DISTINCT ?forecast
                WHERE {
                    <%s> orto:simulationHasForecast ?forecast .
                }
                """.formatted(simulationUri),
                "forecast"
        ));

        return dto;
    }

    private boolean simulationRunExists(Model model, String simulationUri) {
        String askQuery = """
            PREFIX orto: <http://orto.example/>

            ASK {
                <%s> a orto:SimulationRun .
            }
            """.formatted(simulationUri);

        Query query = QueryFactory.create(askQuery);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            return qexec.execAsk();
        }
    }

    private void loadSimulationMetadata(
            Model model,
            String simulationUri,
            RdfSimulationRunDetailDTO dto
    ) {
        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?mode ?createdAt ?garden
            WHERE {
                <%s> a orto:SimulationRun .

                OPTIONAL { <%s> orto:mode ?mode . }
                OPTIONAL { <%s> orto:createdAt ?createdAt . }
                OPTIONAL { <%s> orto:forGarden ?garden . }
            }
            """.formatted(
                simulationUri,
                simulationUri,
                simulationUri,
                simulationUri
        );

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            if (!resultSet.hasNext()) {
                return;
            }

            QuerySolution solution = resultSet.nextSolution();

            dto.setMode(getLiteralString(solution, "mode"));
            dto.setCreatedAt(getLiteralString(solution, "createdAt"));

            RDFNode gardenNode = solution.get("garden");

            if (gardenNode != null) {
                dto.setGardenUri(gardenNode.toString());
            }
        }
    }

    /////////////////////////////////////////
    private void loadHistoryMetadata(
            Model model,
            String simulationUri,
            RdfSimulationRunHistoryDTO dto
    ) {
        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?mode ?createdAt ?garden
            WHERE {
                <%s> a orto:SimulationRun .

                OPTIONAL { <%s> orto:mode ?mode . }
                OPTIONAL { <%s> orto:createdAt ?createdAt . }
                OPTIONAL { <%s> orto:forGarden ?garden . }
            }
            """.formatted(
                simulationUri,
                simulationUri,
                simulationUri,
                simulationUri
        );

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            if (!resultSet.hasNext()) {
                return;
            }

            QuerySolution solution = resultSet.nextSolution();

            dto.setMode(getLiteralString(solution, "mode"));
            dto.setCreatedAt(getLiteralString(solution, "createdAt"));

            RDFNode gardenNode = solution.get("garden");

            if (gardenNode != null) {
                dto.setGardenUri(gardenNode.toString());
            }
        }
    }
    //////////////////////////////////////////////
    private List<RdfPlantHistoryDTO> queryPlantsForSimulation(
            Model model,
            String simulationUri
    ) {
        List<RdfPlantHistoryDTO> plants = new ArrayList<>();

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT DISTINCT ?plant ?name ?speciesName ?growthStage ?storeGDD ?note
            WHERE {
                <%s> orto:simulationHasPlant ?plant .

                OPTIONAL { ?plant orto:name ?name . }
                OPTIONAL { ?plant orto:speciesName ?speciesName . }
                OPTIONAL { ?plant orto:growthStage ?growthStage . }
                OPTIONAL { ?plant orto:storeGDD ?storeGDD . }
                OPTIONAL { ?plant orto:note ?note . }
            }
            ORDER BY ?plant
            """.formatted(simulationUri);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                RdfPlantHistoryDTO dto = new RdfPlantHistoryDTO();

                RDFNode plantNode = solution.get("plant");

                if (plantNode != null) {
                    dto.setPlantUri(plantNode.toString());
                }

                dto.setName(getLiteralString(solution, "name"));
                dto.setSpeciesName(getLiteralString(solution, "speciesName"));
                dto.setGrowthStage(getLiteralString(solution, "growthStage"));
                dto.setStoreGDD(getLiteralDouble(solution, "storeGDD"));
                dto.setNote(getLiteralString(solution, "note"));

                plants.add(dto);
            }
        }

        return plants;
    }

    ///////////////////////////////////////////////
    private List<RdfWeatherHistoryDTO> queryWeatherForSimulation(
            Model model,
            String simulationUri
    ) {
        List<RdfWeatherHistoryDTO> weatherDays = new ArrayList<>();

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT DISTINCT ?weather ?dateTime ?tempMin ?tempMax ?humidity
                            ?precipitation ?windKmh ?uvIndex
            WHERE {
                <%s> orto:simulationHasWeather ?weather .

                OPTIONAL { ?weather orto:dateTime ?dateTime . }
                OPTIONAL { ?weather orto:tempMin ?tempMin . }
                OPTIONAL { ?weather orto:tempMax ?tempMax . }
                OPTIONAL { ?weather orto:umidita ?humidity . }
                OPTIONAL { ?weather orto:precipitazione ?precipitation . }
                OPTIONAL { ?weather orto:windKmh ?windKmh . }
                OPTIONAL { ?weather orto:uvIndex ?uvIndex . }
            }
            ORDER BY ?dateTime
            """.formatted(simulationUri);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                RdfWeatherHistoryDTO dto = new RdfWeatherHistoryDTO();

                RDFNode weatherNode = solution.get("weather");

                if (weatherNode != null) {
                    dto.setWeatherUri(weatherNode.toString());
                }

                dto.setDateTime(getLiteralString(solution, "dateTime"));
                dto.setTempMin(getLiteralDouble(solution, "tempMin"));
                dto.setTempMax(getLiteralDouble(solution, "tempMax"));
                dto.setHumidity(getLiteralDouble(solution, "humidity"));
                dto.setPrecipitation(getLiteralDouble(solution, "precipitation"));
                dto.setWindKmh(getLiteralDouble(solution, "windKmh"));
                dto.setUvIndex(getLiteralDouble(solution, "uvIndex"));

                weatherDays.add(dto);
            }
        }

        return weatherDays;
    }

    ////////////////////////////////////////////////
    /////////////////////////////////////////////
    private List<RdfRiskHistoryDTO> queryRisksForSimulation(
            Model model,
            String simulationUri
    ) {
        List<RdfRiskHistoryDTO> risks = new ArrayList<>();

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT DISTINCT ?plant ?plantName ?weather ?dateTime
                            ?risk ?riskCaldo ?riskFreddo ?riskVento
                            ?riskMalattia ?consigli
            WHERE {
                <%s> orto:simulationHasPlant ?plant .
                <%s> orto:simulationHasRisk ?risk .
                <%s> orto:simulationHasWeather ?weather .

                ?plant orto:hasRisk ?risk .
                ?weather orto:generatesRisk ?risk .

                OPTIONAL { ?plant orto:name ?plantName . }
                OPTIONAL { ?weather orto:dateTime ?dateTime . }

                OPTIONAL { ?risk orto:riskCaldo ?riskCaldo . }
                OPTIONAL { ?risk orto:riskFreddo ?riskFreddo . }
                OPTIONAL { ?risk orto:riskVento ?riskVento . }
                OPTIONAL { ?risk orto:riskMalattia ?riskMalattia . }
                OPTIONAL { ?risk orto:consigli ?consigli . }
            }
            ORDER BY ?plant ?dateTime ?risk
            """.formatted(simulationUri, simulationUri, simulationUri);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                RdfRiskHistoryDTO dto = new RdfRiskHistoryDTO();

                RDFNode plantNode = solution.get("plant");
                RDFNode weatherNode = solution.get("weather");
                RDFNode riskNode = solution.get("risk");

                if (plantNode != null) {
                    dto.setPlantUri(plantNode.toString());
                }

                if (weatherNode != null) {
                    dto.setWeatherUri(weatherNode.toString());
                }

                if (riskNode != null) {
                    dto.setRiskUri(riskNode.toString());
                }

                dto.setPlantName(getLiteralString(solution, "plantName"));
                dto.setDateTime(getLiteralString(solution, "dateTime"));

                dto.setRiskCaldo(getLiteralString(solution, "riskCaldo"));
                dto.setRiskFreddo(getLiteralString(solution, "riskFreddo"));
                dto.setRiskVento(getLiteralString(solution, "riskVento"));
                dto.setRiskMalattia(getLiteralString(solution, "riskMalattia"));
                dto.setConsigli(getLiteralString(solution, "consigli"));

                risks.add(dto);
            }
        }

        return risks;
    }





    //////////////////////////////
    private List<RdfForecastHistoryDTO> queryForecastsForSimulation(
            Model model,
            String simulationUri
    ) {
        List<RdfForecastHistoryDTO> forecasts = new ArrayList<>();

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT DISTINCT ?forecast ?dateTime ?gddDaily ?percentCiclo
                            ?growthStage ?daysToMaturity
            WHERE {
                <%s> orto:simulationHasForecast ?forecast .

                OPTIONAL { ?forecast orto:dateTime ?dateTime . }
                OPTIONAL { ?forecast orto:gddDaily ?gddDaily . }
                OPTIONAL { ?forecast orto:percentCiclo ?percentCiclo . }
                OPTIONAL { ?forecast orto:growthStage ?growthStage . }
                OPTIONAL { ?forecast orto:daysToMaturity ?daysToMaturity . }
            }
            ORDER BY ?dateTime
            """.formatted(simulationUri);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                RdfForecastHistoryDTO dto = new RdfForecastHistoryDTO();

                RDFNode forecastNode = solution.get("forecast");

                if (forecastNode != null) {
                    dto.setForecastUri(forecastNode.toString());
                }

                dto.setDateTime(getLiteralString(solution, "dateTime"));
                dto.setGddDaily(getLiteralDouble(solution, "gddDaily"));
                dto.setPercentCiclo(getLiteralDouble(solution, "percentCiclo"));
                dto.setGrowthStage(getLiteralString(solution, "growthStage"));
                dto.setDaysToMaturity(getLiteralInteger(solution, "daysToMaturity"));

                forecasts.add(dto);
            }
        }

        return forecasts;
    }



    private List<String> querySingleColumn(
            Model model,
            String queryString,
            String variableName
    ) {
        List<String> values = new ArrayList<>();

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();
                RDFNode node = solution.get(variableName);

                if (node != null) {
                    values.add(node.toString());
                }
            }
        }

        return values;
    }


    private String getLiteralString(QuerySolution solution, String variableName) {
        RDFNode node = solution.get(variableName);

        if (node == null || !node.isLiteral()) {
            return null;
        }

        return node.asLiteral().getString();
    }

    private Integer getLiteralInteger(QuerySolution solution, String variableName) {
        RDFNode node = solution.get(variableName);

        if (node == null || !node.isLiteral()) {
            return 0;
        }

        Literal literal = node.asLiteral();

        try {
            return literal.getInt();
        } catch (Exception e) {
            try {
                return (int) literal.getLong();
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    private Double getLiteralDouble(QuerySolution solution, String variableName) {
        RDFNode node = solution.get(variableName);

        if (node == null || !node.isLiteral()) {
            return null;
        }

        Literal literal = node.asLiteral();

        try {
            return literal.getDouble();
        } catch (Exception e) {
            return null;
        }
    }

    private String extractRunId(String simulationUri) {
        if (simulationUri == null || simulationUri.isBlank()) {
            return null;
        }

        int index = simulationUri.lastIndexOf("/");

        if (index < 0 || index == simulationUri.length() - 1) {
            return simulationUri;
        }

        return simulationUri.substring(index + 1);
    }
}