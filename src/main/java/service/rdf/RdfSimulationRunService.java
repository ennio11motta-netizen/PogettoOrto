package service.rdf;


import dto.rdf.*;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class RdfSimulationRunService {

    private static final String NS = "http://orto.example/";

    private final RdfService rdfService;
    private final RdfQueryService rdfQueryService;

    public RdfSimulationRunService(
            RdfService rdfService,
            RdfQueryService rdfQueryService
    ) {
        this.rdfService = rdfService;
        this.rdfQueryService = rdfQueryService;
    }


    // =========================================================
    // HISTORY COMPLETO PER CARD 8
    // =========================================================

    public List<RdfSimulationRunHistoryDTO> getSimulationHistoryByGarden(Integer locationId) {
        if (locationId == null) {
            throw new IllegalArgumentException("locationId obbligatorio");
        }

        String gardenUri = NS + "garden/" + locationId;
        Model model = rdfService.getModel();

        List<String> simulationUris = rdfQueryService.querySingleColumn(
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

            dto.setMode(rdfQueryService.getLiteralString(solution, "mode"));
            dto.setCreatedAt(rdfQueryService.getLiteralString(solution, "createdAt"));

            RDFNode gardenNode = solution.get("garden");

            if (gardenNode != null) {
                dto.setGardenUri(gardenNode.toString());
            }
        }
    }

    // =========================================================
    // QUERY PIANte
    // =========================================================

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

                dto.setName(rdfQueryService.getLiteralString(solution, "name"));
                dto.setSpeciesName(rdfQueryService.getLiteralString(solution, "speciesName"));
                dto.setGrowthStage(rdfQueryService.getLiteralString(solution, "growthStage"));
                dto.setStoreGDD(rdfQueryService.getLiteralDouble(solution, "storeGDD"));
                dto.setNote(rdfQueryService.getLiteralString(solution, "note"));

                plants.add(dto);
            }
        }

        return plants;
    }

    // =========================================================
    // QUERY METEO
    // =========================================================

    private List<RdfWeatherHistoryDTO> queryWeatherForSimulation(
            Model model,
            String simulationUri
    ) {
//        List<RdfWeatherHistoryDTO> weatherDays = new ArrayList<>();

        Map<String, RdfWeatherHistoryDTO> weatherByUri = new LinkedHashMap<>();

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

                dto.setDateTime(rdfQueryService.getLiteralString(solution, "dateTime"));
                dto.setTempMin(rdfQueryService.getLiteralDouble(solution, "tempMin"));
                dto.setTempMax(rdfQueryService.getLiteralDouble(solution, "tempMax"));
                dto.setHumidity(rdfQueryService.getLiteralDouble(solution, "humidity"));
                dto.setPrecipitation(rdfQueryService.getLiteralDouble(solution, "precipitation"));
                dto.setWindKmh(rdfQueryService.getLiteralDouble(solution, "windKmh"));
                dto.setUvIndex(rdfQueryService.getLiteralDouble(solution, "uvIndex"));


                if (dto.getWeatherUri() != null) {
                    weatherByUri.putIfAbsent(dto.getWeatherUri(), dto);
                }
            }
        }

        return new ArrayList<>(weatherByUri.values());
    }


    // =========================================================
    // QUERY RISCHI
    // =========================================================

    private List<RdfRiskHistoryDTO> queryRisksForSimulation(
            Model model,
            String simulationUri
    ) {
//        List<RdfRiskHistoryDTO> risks = new ArrayList<>();
        Map<String, RdfRiskHistoryDTO> risksByUri = new LinkedHashMap<>();

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

                dto.setPlantName(rdfQueryService.getLiteralString(solution, "plantName"));
                dto.setDateTime(rdfQueryService.getLiteralString(solution, "dateTime"));

                dto.setRiskCaldo(rdfQueryService.getLiteralString(solution, "riskCaldo"));
                dto.setRiskFreddo(rdfQueryService.getLiteralString(solution, "riskFreddo"));
                dto.setRiskVento(rdfQueryService.getLiteralString(solution, "riskVento"));
                dto.setRiskMalattia(rdfQueryService.getLiteralString(solution, "riskMalattia"));
                dto.setConsigli(rdfQueryService.getLiteralString(solution, "consigli"));


                if (dto.getRiskUri() != null) {
                    risksByUri.putIfAbsent(dto.getRiskUri(), dto);
                }

            }
        }

        return new ArrayList<>(risksByUri.values());
    }

    // =========================================================
    // QUERY FORECAST
    // =========================================================

    private List<RdfForecastHistoryDTO> queryForecastsForSimulation(
            Model model,
            String simulationUri
    ) {
        List<RdfForecastHistoryDTO> forecasts = new ArrayList<>();

        Map<String, RdfForecastHistoryDTO> forecastsByUri = new LinkedHashMap<>();

        String queryString = """
        PREFIX orto: <http://orto.example/>

        SELECT DISTINCT ?plant ?plantName ?forecast ?weather ?weatherDateTime
                        ?forecastCreatedAt ?gddDaily ?percentCiclo
                        ?growthStage ?daysToMaturity
        WHERE {
            <%s> orto:simulationHasPlant ?plant .
            <%s> orto:simulationHasForecast ?forecast .

            ?plant orto:hasForecast ?forecast .

            OPTIONAL { ?plant orto:name ?plantName . }

            OPTIONAL { ?forecast orto:basedOnWeather ?weather . }
            OPTIONAL { ?weather orto:dateTime ?weatherDateTime . }

            OPTIONAL { ?forecast orto:dateTime ?forecastCreatedAt . }
            OPTIONAL { ?forecast orto:gddDaily ?gddDaily . }
            OPTIONAL { ?forecast orto:percentCiclo ?percentCiclo . }
            OPTIONAL { ?forecast orto:growthStage ?growthStage . }
            OPTIONAL { ?forecast orto:daysToMaturity ?daysToMaturity . }
        }
        ORDER BY ?plant ?weatherDateTime ?forecast
        """.formatted(simulationUri, simulationUri);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                RdfForecastHistoryDTO dto = new RdfForecastHistoryDTO();

                RDFNode plantNode = solution.get("plant");
                RDFNode forecastNode = solution.get("forecast");
                RDFNode weatherNode = solution.get("weather");

                if (plantNode != null) {
                    dto.setPlantUri(plantNode.toString());
                }

                if (forecastNode != null) {
                    dto.setForecastUri(forecastNode.toString());
                }


                if (weatherNode != null) {
                    dto.setWeatherUri(weatherNode.toString());
                }


                dto.setPlantName(rdfQueryService.getLiteralString(solution, "plantName"));


               dto.setWeatherDateTime(rdfQueryService.getLiteralString(solution, "weatherDateTime"));



                dto.setForecastCreatedAt(rdfQueryService.getLiteralString(solution, "forecastCreatedAt"));

                dto.setGddDaily(rdfQueryService.getLiteralDouble(solution, "gddDaily"));
                dto.setPercentCiclo(rdfQueryService.getLiteralDouble(solution, "percentCiclo"));
                dto.setGrowthStage(rdfQueryService.getLiteralString(solution, "growthStage"));
                dto.setDaysToMaturity(rdfQueryService.getLiteralInteger(solution, "daysToMaturity"));


                if (dto.getForecastUri() != null) {
                    forecastsByUri.putIfAbsent(dto.getForecastUri(), dto);
                }
            }
        }

        return new ArrayList<>(forecastsByUri.values());
    }

    // =========================================================
    // UTILITY LOCALE
    // =========================================================

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