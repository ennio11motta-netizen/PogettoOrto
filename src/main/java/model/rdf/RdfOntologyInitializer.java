package model.rdf;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import static model.rdf.RdfVocabulary.*;

@Component
public class RdfOntologyInitializer {

    public void initialize(Model model) {
        if (model == null) {
            throw new IllegalArgumentException("Model RDF non può essere null");
        }

        initLabels(model);
        initHierarchy(model);
        initDomainRange(model);
    }

    private void initLabels(Model model) {
        model.add(NAME, RDFS.label, model.createLiteral("nome", "it"));
        model.add(LATITUDE, RDFS.label, model.createLiteral("latitudine", "it"));
        model.add(LONGITUDE, RDFS.label, model.createLiteral("longitudine", "it"));
        model.add(HAS_PLANT, RDFS.label, model.createLiteral("ha pianta", "it"));
        model.add(BELONGS_TO_GARDEN, RDFS.label, model.createLiteral("appartiene all'orto", "it"));

        model.add(GARDEN_CLASS, RDFS.label, model.createLiteral("orto", "it"));

        model.add(DATE_TIME, RDFS.label, model.createLiteral("data e ora", "it"));
        model.add(WIND_KMH, RDFS.label, model.createLiteral("vento km/h", "it"));
        model.add(UV_INDEX, RDFS.label, model.createLiteral("indice UV", "it"));
        model.add(GDD_DAILY, RDFS.label, model.createLiteral("GDD giornaliero", "it"));
        model.add(PERCENT_CICLO, RDFS.label, model.createLiteral("percentuale ciclo", "it"));
        model.add(DAYS_TO_MATURITY, RDFS.label, model.createLiteral("giorni alla maturazione", "it"));
        model.add(BASED_ON_WEATHER, RDFS.label, model.createLiteral("basato sul meteo", "it"));

        model.add(NOTE, RDFS.label, model.createLiteral("note", "it"));
        model.add(SPECIES_NAME, RDFS.label, model.createLiteral("nome specie", "it"));

        model.add(MODE, RDFS.label, model.createLiteral("modalità", "it"));
        model.add(CREATED_AT, RDFS.label, model.createLiteral("creata il", "it"));
        model.add(FOR_GARDEN, RDFS.label, model.createLiteral("per orto", "it"));
        model.add(SIMULATION_HAS_PLANT, RDFS.label, model.createLiteral("simulazione ha pianta", "it"));
        model.add(SIMULATION_HAS_WEATHER, RDFS.label, model.createLiteral("simulazione ha meteo", "it"));
        model.add(SIMULATION_HAS_RISK, RDFS.label, model.createLiteral("simulazione ha rischio", "it"));
        model.add(SIMULATION_HAS_FORECAST, RDFS.label, model.createLiteral("simulazione ha previsione crescita", "it"));

        model.add(SIMULATION_RUN_CLASS, RDFS.label, model.createLiteral("simulazione", "it"));
        model.add(PREVIEW_SIMULATION_CLASS, RDFS.label, model.createLiteral("simulazione previsionale", "it"));
        model.add(APPLIED_SIMULATION_CLASS, RDFS.label, model.createLiteral("simulazione applicata", "it"));
    }

    private void initHierarchy(Model model) {
        model.add(CRITICAL_RISK, RDFS.subClassOf, RISK_CLASS);
        model.add(MODERATE_RISK, RDFS.subClassOf, RISK_CLASS);
        model.add(LOW_RISK, RDFS.subClassOf, RISK_CLASS);

        model.add(ADVANCED_STAGE, RDFS.subClassOf, GROWTH_STAGE_CLASS);
        model.add(EARLY_STAGE, RDFS.subClassOf, GROWTH_STAGE_CLASS);

        model.add(PREVIEW_SIMULATION_CLASS, RDFS.subClassOf, SIMULATION_RUN_CLASS);
        model.add(APPLIED_SIMULATION_CLASS, RDFS.subClassOf, SIMULATION_RUN_CLASS);
    }

    private void initDomainRange(Model model) {
        model.add(HAS_RISK, RDFS.domain, PLANT_CLASS);
        model.add(HAS_RISK, RDFS.range, RISK_CLASS);

        model.add(HAS_FORECAST, RDFS.domain, PLANT_CLASS);
        model.add(HAS_FORECAST, RDFS.range, FORECAST_CLASS);


        model.add(BASED_ON_WEATHER, RDFS.domain, FORECAST_CLASS);
        model.add(BASED_ON_WEATHER, RDFS.range, WEATHER_CLASS);


        model.add(GENERATES_RISK, RDFS.domain, WEATHER_CLASS);
        model.add(GENERATES_RISK, RDFS.range, RISK_CLASS);

        model.add(INFLUENCED_BY, RDFS.domain, PLANT_CLASS);
        model.add(INFLUENCED_BY, RDFS.range, WEATHER_CLASS);

        model.add(HAS_PLANT, RDFS.domain, GARDEN_CLASS);
        model.add(HAS_PLANT, RDFS.range, PLANT_CLASS);

        model.add(BELONGS_TO_GARDEN, RDFS.domain, PLANT_CLASS);
        model.add(BELONGS_TO_GARDEN, RDFS.range, GARDEN_CLASS);

        model.add(MODE, RDFS.domain, SIMULATION_RUN_CLASS);
        model.add(MODE, RDFS.range, RDFS.Literal);

        model.add(CREATED_AT, RDFS.domain, SIMULATION_RUN_CLASS);
        model.add(CREATED_AT, RDFS.range, RDFS.Literal);

        model.add(FOR_GARDEN, RDFS.domain, SIMULATION_RUN_CLASS);
        model.add(FOR_GARDEN, RDFS.range, GARDEN_CLASS);

        model.add(SIMULATION_HAS_PLANT, RDFS.domain, SIMULATION_RUN_CLASS);
        model.add(SIMULATION_HAS_PLANT, RDFS.range, PLANT_CLASS);

        model.add(SIMULATION_HAS_WEATHER, RDFS.domain, SIMULATION_RUN_CLASS);
        model.add(SIMULATION_HAS_WEATHER, RDFS.range, WEATHER_CLASS);

        model.add(SIMULATION_HAS_RISK, RDFS.domain, SIMULATION_RUN_CLASS);
        model.add(SIMULATION_HAS_RISK, RDFS.range, RISK_CLASS);

        model.add(SIMULATION_HAS_FORECAST, RDFS.domain, SIMULATION_RUN_CLASS);
        model.add(SIMULATION_HAS_FORECAST, RDFS.range, FORECAST_CLASS);
    }
}