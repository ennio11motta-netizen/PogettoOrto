package service.rdf;

import dto.rdf.RdfGardenDetailDTO;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.stereotype.Service;
import dto.rdf.RdfWeatherRiskRelationDTO;
import java.util.ArrayList;
import java.util.List;

@Service
public class RdfGardenDetailService {

    private static final String NS = "http://orto.example/";

    private final RdfService rdfService;

    public RdfGardenDetailService(RdfService rdfService) {
        this.rdfService = rdfService;
    }

    public RdfGardenDetailDTO getGardenDetail(Integer locationId) {
        if (locationId == null) {
            throw new IllegalArgumentException("locationId obbligatorio");
        }

        String gardenUri = NS + "garden/" + locationId;

        Model model = rdfService.getModel();

        if (!gardenExists(model, gardenUri)) {
            throw new IllegalArgumentException(
                    "Orto non trovato nel grafo RDF con id: " + locationId
            );
        }

        RdfGardenDetailDTO dto = new RdfGardenDetailDTO();
        dto.setLocationId(locationId);
        dto.setGardenUri(gardenUri);

        loadGardenMetadata(model, gardenUri, dto);

        dto.setPlantUris(querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>

                SELECT DISTINCT ?plant
                WHERE {
                    <%s> orto:hasPlant ?plant .
                }
                """.formatted(gardenUri),
                "plant"
        ));

        dto.setRiskUris(querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>

                SELECT DISTINCT ?risk
                WHERE {
                    <%s> orto:hasPlant ?plant .
                    ?plant orto:hasRisk ?risk .
                }
                """.formatted(gardenUri),
                "risk"
        ));

        dto.setForecastUris(querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>

                SELECT DISTINCT ?forecast
                WHERE {
                    <%s> orto:hasPlant ?plant .
                    ?plant orto:hasForecast ?forecast .
                }
                """.formatted(gardenUri),
                "forecast"
        ));

        dto.setDangerPlantUris(querySingleColumn(
                model,
                """
                PREFIX orto: <http://orto.example/>

                SELECT DISTINCT ?plant
                WHERE {
                    <%s> orto:hasPlant ?plant .
                    ?plant orto:isInDanger true .
                }
                """.formatted(gardenUri),
                "plant"
        ));

        dto.setWeatherRiskRelations(queryWeatherRiskRelations(model, gardenUri));

        return dto;
    }

    private boolean gardenExists(Model model, String gardenUri) {
        String askQuery = """
                PREFIX orto: <http://orto.example/>

                ASK {
                    <%s> a orto:Garden .
                }
                """.formatted(gardenUri);

        Query query = QueryFactory.create(askQuery);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            return qexec.execAsk();
        }
    }

    private void loadGardenMetadata(
            Model model,
            String gardenUri,
            RdfGardenDetailDTO dto
    ) {
        String queryString = """
                PREFIX orto: <http://orto.example/>

                SELECT ?name ?latitude ?longitude
                WHERE {
                    <%s> a orto:Garden .

                    OPTIONAL { <%s> orto:name ?name . }
                    OPTIONAL { <%s> orto:latitude ?latitude . }
                    OPTIONAL { <%s> orto:longitude ?longitude . }
                }
                """.formatted(gardenUri, gardenUri, gardenUri, gardenUri);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            if (!resultSet.hasNext()) {
                return;
            }

            QuerySolution solution = resultSet.nextSolution();

            dto.setGardenName(getLiteralString(solution, "name"));
            dto.setLatitude(getLiteralDouble(solution, "latitude"));
            dto.setLongitude(getLiteralDouble(solution, "longitude"));
        }
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

    private List<RdfWeatherRiskRelationDTO> queryWeatherRiskRelations(
            Model model,
            String gardenUri
    ) {
        List<RdfWeatherRiskRelationDTO> relations = new ArrayList<>();

        String queryString = """
                PREFIX orto: <http://orto.example/>

                SELECT DISTINCT ?weather ?risk
                WHERE {
                    <%s> orto:hasPlant ?plant .
                    ?plant orto:influencedBy ?weather .
                    ?weather orto:generatesRisk ?risk .
                }
                """.formatted(gardenUri);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet resultSet = qexec.execSelect();

            while (resultSet.hasNext()) {
                QuerySolution solution = resultSet.nextSolution();

                RDFNode weatherNode = solution.get("weather");
                RDFNode riskNode = solution.get("risk");

                if (weatherNode != null && riskNode != null) {
                    relations.add(new RdfWeatherRiskRelationDTO(
                            weatherNode.toString(),
                            riskNode.toString()
                    ));
                }
            }
        }

        return relations;
    }

    private String getLiteralString(QuerySolution solution, String variableName) {
        RDFNode node = solution.get(variableName);

        if (node == null || !node.isLiteral()) {
            return null;
        }

        return node.asLiteral().getString();
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
}
