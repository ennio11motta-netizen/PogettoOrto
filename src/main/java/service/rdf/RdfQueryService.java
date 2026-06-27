package service.rdf;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


import org.apache.jena.query.*;

/**
 * Servizio di supporto per query SPARQL sul modello RDF.
 *
 * Responsabilità:
 * - eseguire query SPARQL comuni
 * - estrarre valori RDFNode/literal
 * - evitare duplicazione di helper nei servizi RDF
 */
@Service
public class RdfQueryService {

    public List<String> querySingleColumn(
            Model model,
            String queryString,
            String variableName
    ) {
        validateModelAndQuery(model, queryString);

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

    public boolean ask(
            Model model,
            String askQueryString
    ) {
        validateModelAndQuery(model, askQueryString);

        Query query = QueryFactory.create(askQueryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            return qexec.execAsk();
        }
    }

    public String getLiteralString(
            QuerySolution solution,
            String variableName
    ) {
        RDFNode node = solution.get(variableName);

        if (node == null || !node.isLiteral()) {
            return null;
        }

        return node.asLiteral().getString();
    }

    public Double getLiteralDouble(
            QuerySolution solution,
            String variableName
    ) {
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

    public Integer getLiteralInteger(
            QuerySolution solution,
            String variableName
    ) {
        RDFNode node = solution.get(variableName);

        if (node == null || !node.isLiteral()) {
            return null;
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

    public RDFNode getNode(
            QuerySolution solution,
            String variableName
    ) {
        return solution.get(variableName);
    }

    /**
     * Metodo opzionale solo per debug.
     * Non usarlo nel flusso applicativo della card 8.
     */
    public void printSelect(
            Model model,
            String queryString
    ) {
        validateModelAndQuery(model, queryString);

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }
    }

    private void validateModelAndQuery(
            Model model,
            String queryString
    ) {
        if (model == null) {
            throw new IllegalArgumentException("Model RDF non può essere null");
        }

        if (queryString == null || queryString.isBlank()) {
            throw new IllegalArgumentException("Query SPARQL obbligatoria");
        }
    }
}
