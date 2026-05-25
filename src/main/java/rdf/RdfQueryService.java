package rdf;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

/**
 * Service per query SPARQL sul modello RDF (con reasoning).
 */
public class RdfQueryService {

    private final Model model;

    public RdfQueryService(Model model) {
        this.model = model;
    }

    /**
     * Recupera le percentuali di crescita (dato numerico).
     */
    public void queryPercentualiCrescita() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?f ?percentuale
            WHERE {
                ?f a orto:GrowthForecast ;
                   orto:percentCiclo ?percentuale .
            }
        """;

        esegui(queryString);
    }

    /**
     * Metodo generico per eseguire query.
     */
    public void esegui(String queryString) {

        Query query = QueryFactory.create(queryString);

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        }
    }

    // =========================================================
    // ✅ QUERY SEMANTICHE CON REASONING
    // =========================================================

    /**
     * Recupera TUTTI i rischi (grazie a subClassOf + reasoning).
     */
    public void queryTuttiIRischi() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?risk
            WHERE {
                ?risk a orto:Risk .
            }
        """;

        esegui(queryString);
    }

    /**
     * Recupera solo rischi critici (specifico).
     */
    public void queryRischiCritici() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?risk
            WHERE {
                ?risk a orto:CriticalRisk .
            }
        """;

        esegui(queryString);
    }

    /**
     * Recupera TUTTE le piante (grazie a ontologia).
     */
    public void queryTutteLePiante() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?plant
            WHERE {
                ?plant a orto:Plant .
            }
        """;

        esegui(queryString);
    }

    /**
     * Recupera piante con QUALSIASI rischio
     * (grazie a reasoning sulle classi Risk).
     */
    public void queryPianteConRischio() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?plant ?risk
            WHERE {
                ?plant orto:hasRisk ?risk .
                ?risk a orto:Risk .
            }
        """;

        esegui(queryString);
    }

    /**
     * Recupera condizioni meteo che generano rischi
     * (senza specificare il tipo grazie a reasoning).
     */
    public void queryMeteoRischiGenerici() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?weather ?risk
            WHERE {
                ?weather orto:generatesRisk ?risk .
                ?risk a orto:Risk .
            }
        """;

        esegui(queryString);
    }

    /**
     * Recupera tutte le piante con GROWTH STAGE
     * usando la gerarchia (EARLY + ADVANCED).
     */
    public void queryPiantePerStadio() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?plant
            WHERE {
                ?plant a orto:GrowthStage .
            }
        """;

        esegui(queryString);
    }

    /**
     * Query decisionale: piante avanzate con qualsiasi rischio.
     */
    public void queryPianteAvanzateConRischio() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT DISTINCT ?plant
            WHERE {
                ?plant a orto:AdvancedGrowthStage .
                ?plant orto:hasRisk ?risk .
                ?risk a orto:Risk .
            }
        """;

        esegui(queryString);
    }

    /**
     * Query dopo inferenza:
     * trova piante automaticamente classificate "in pericolo".
     */
    public void queryPianteInPericolo() {

        String queryString = """
            PREFIX orto: <http://orto.example/>

            SELECT ?plant
            WHERE {
                ?plant orto:isInDanger true .
            }
        """;

        esegui(queryString);
    }
}