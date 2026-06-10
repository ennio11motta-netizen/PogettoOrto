package util;



import exception.GrowthStage;
import exception.IrrigationLevel;
import model.PlantInstance;
import model.WeatherDay;

public class IrrigationCalculator {

    public IrrigationResult valutaIrrigazione(
            PlantInstance plant,
            WeatherDay weatherDay
    ) {
        validateInput(plant, weatherDay);

        Double tempMax = weatherDay.getTempMax();
        Double precipitazione = weatherDay.getPrecipitazione();
        Double umidita = weatherDay.getUmidita();
        GrowthStage growthStage = plant.getGrowthStage();

        double safeTempMax = tempMax != null ? tempMax : 0.0;
        double safePrecipitazione = precipitazione != null ? precipitazione : 0.0;
        double safeUmidita = umidita != null ? umidita : 100.0;

        /*
         * 1. Pioggia sufficiente.
         * Se sono previsti almeno 5 mm, consideriamo l'irrigazione non necessaria.
         */
        if (safePrecipitazione >= 5.0) {
            return new IrrigationResult(
                    IrrigationLevel.NONE,
                    "Non annaffiare: le precipitazioni previste sono sufficienti."
            );
        }

        /*
         * 2. Caldo intenso e quasi assenza di pioggia.
         */
        if (safeTempMax >= 32.0 && safePrecipitazione < 1.0) {
            return new IrrigationResult(
                    IrrigationLevel.HIGH,
                    "Annaffiare con priorità, preferibilmente nelle ore serali: temperatura elevata e precipitazioni quasi assenti."
            );
        }

        /*
         * 3. Fasi sensibili della pianta.
         * Fioritura e fruttificazione richiedono più attenzione idrica.
         */
        if (isFaseSensibile(growthStage) && safePrecipitazione < 1.0 && safeTempMax >= 27.0) {
            return new IrrigationResult(
                    IrrigationLevel.HIGH,
                    "Annaffiare con attenzione: la pianta è in una fase sensibile e sono previste temperature alte con poca pioggia."
            );
        }

        /*
         * 4. Caldo moderato e poca pioggia.
         */
        if (safeTempMax >= 27.0 && safePrecipitazione < 2.0) {
            return new IrrigationResult(
                    IrrigationLevel.MEDIUM,
                    "Annaffiare moderatamente: temperature medio-alte e precipitazioni scarse."
            );
        }

        /*
         * 5. Umidità bassa e quasi assenza di pioggia.
         */
        if (safeUmidita < 45.0 && safePrecipitazione < 1.0) {
            return new IrrigationResult(
                    IrrigationLevel.MEDIUM,
                    "Annaffiare moderatamente: umidità bassa e assenza di pioggia significativa."
            );
        }

        /*
         * 6. Poca pioggia ma condizioni non critiche.
         */
        if (safePrecipitazione < 1.0) {
            return new IrrigationResult(
                    IrrigationLevel.LOW,
                    "Controllare il terreno: potrebbe essere utile una leggera irrigazione se il suolo è asciutto."
            );
        }

        /*
         * 7. Caso normale.
         */
        return new IrrigationResult(
                IrrigationLevel.NONE,
                "Non è necessario annaffiare: le condizioni meteo non indicano stress idrico rilevante."
        );
    }

    private boolean isFaseSensibile(GrowthStage growthStage) {
        return growthStage == GrowthStage.FIORITURA
                || growthStage == GrowthStage.FRUTTIFICAZIONE;
    }

    private void validateInput(PlantInstance plant, WeatherDay weatherDay) {
        if (plant == null) {
            throw new IllegalArgumentException("PlantInstance non può essere null");
        }

        if (weatherDay == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }
    }
}
