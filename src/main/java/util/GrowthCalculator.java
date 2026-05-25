package util;

import exception.GrowthStage;
import model.PlantInstance;
import model.PlantSpecie;
import model.WeatherDay;

/**
 * Classe utility responsabile dei calcoli legati alla crescita delle piante.
 * NON accede al database e NON gestisce logica applicativa.
 * Si occupa solo di:
 * - calcolo GDD
 * - determinazione stadio
 * - percentuale ciclo
 * - stime crescita
 */
public class GrowthCalculator {

    /**
     * Calcola il GDD (Growing Degree Days) giornaliero.
     */
    public Double calcolaGDDGiornaliero(PlantSpecie specie, WeatherDay weatherDay) {

        if (specie == null || weatherDay == null) {
            throw new IllegalArgumentException("Specie e WeatherDay non possono essere null");
        }

        if (specie.getTempBase() == null ||
                weatherDay.getTempMin() == null ||
                weatherDay.getTempMax() == null) {
            throw new IllegalArgumentException("Dati temperatura mancanti");
        }

        double media = (weatherDay.getTempMin() + weatherDay.getTempMax()) / 2.0;
        double gdd = media - specie.getTempBase();

        // GDD non può essere negativo
        return Math.max(gdd, 0.0);
    }

    /**
     * Aggiorna i GDD accumulati nella pianta.
     */
    public void aggiornaGDDAccumulati(PlantInstance pianta, WeatherDay weatherDay) {

        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        PlantSpecie specie = pianta.getPlantSpecie();
        if (specie == null) {
            throw new IllegalArgumentException("Specie mancante nella pianta");
        }

        Double gddGiornaliero = calcolaGDDGiornaliero(specie, weatherDay);

        Double storeGDD = (pianta.getStoreGDD() != null)
                ? pianta.getStoreGDD()
                : 0.0;

        pianta.setStoreGDD(storeGDD + gddGiornaliero);
    }

    /**
     * Determina lo stadio di crescita in base ai GDD accumulati.
     */
    public GrowthStage determinaStadio(PlantSpecie specie, Double gddAccumulati) {

        if (specie == null) {
            throw new IllegalArgumentException("PlantSpecie non può essere null");
        }

        if (gddAccumulati == null) {
            gddAccumulati = 0.0;
        }

        // ordine dal più avanzato al più iniziale
        if (specie.getGddMaturazione() != null && gddAccumulati >= specie.getGddMaturazione()) {
            return GrowthStage.MATURAZIONE;
        }

        if (specie.getGddFruttificazione() != null && gddAccumulati >= specie.getGddFruttificazione()) {
            return GrowthStage.FRUTTIFICAZIONE;
        }

        if (specie.getGddFioritura() != null && gddAccumulati >= specie.getGddFioritura()) {
            return GrowthStage.FIORITURA;
        }

        if (specie.getGddSviluppo() != null && gddAccumulati >= specie.getGddSviluppo()) {
            return GrowthStage.VEGETATIVO;
        }

        if (specie.getGddEmergenza() != null && gddAccumulati >= specie.getGddEmergenza()) {
            return GrowthStage.EMERGENZA;
        }

        return GrowthStage.SEMINA;
    }

    /**
     * Aggiorna lo stadio della pianta.
     */
    public void aggiornaStadio(PlantInstance pianta) {

        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        PlantSpecie specie = pianta.getPlantSpecie();
        if (specie == null) {
            throw new IllegalArgumentException("Specie mancante");
        }

        GrowthStage nuovoStadio =
                determinaStadio(specie, pianta.getStoreGDD());

        pianta.setGrowthStage(nuovoStadio);
    }

    /**
     * Calcola la percentuale di completamento del ciclo.
     */
    public Double calcolaPercentualeCiclo(PlantSpecie specie, Double gddAccumulati) {

        if (specie == null) {
            throw new IllegalArgumentException("PlantSpecie non può essere null");
        }

        if (specie.getGddMaturazione() == null || specie.getGddMaturazione() <= 0) {
            return 0.0;
        }

        if (gddAccumulati == null) {
            gddAccumulati = 0.0;
        }

        double percentuale =
                (gddAccumulati / specie.getGddMaturazione()) * 100.0;

        // clamp tra 0 e 100
        return Math.max(0.0, Math.min(percentuale, 100.0));
    }

    /**
     * Aggiorna completamente la crescita:
     * - GDD
     * - stadio
     */
    public void aggiornaCrescita(PlantInstance pianta, WeatherDay weatherDay) {
        aggiornaGDDAccumulati(pianta, weatherDay);
        aggiornaStadio(pianta);
    }

    /**
     * Stima i giorni mancanti alla maturazione.
     */
    public Integer stimaGiorniAllaMaturazione(
            PlantInstance pianta,
            Double gddMedioPrevisto) {

        if (pianta == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        PlantSpecie specie = pianta.getPlantSpecie();
        if (specie == null) {
            throw new IllegalArgumentException("Specie mancante");
        }

        if (specie.getGddMaturazione() == null) {
            return null;
        }

        if (gddMedioPrevisto == null || gddMedioPrevisto <= 0) {
            return null;
        }

        double gddAttuali =
                (pianta.getStoreGDD() != null)
                        ? pianta.getStoreGDD()
                        : 0.0;

        double gddMancanti = specie.getGddMaturazione() - gddAttuali;

        if (gddMancanti <= 0) {
            return 0;
        }

        return (int) Math.ceil(gddMancanti / gddMedioPrevisto);
    }
}