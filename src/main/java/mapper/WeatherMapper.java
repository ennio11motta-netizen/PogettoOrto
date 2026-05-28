package mapper;




import dto.WeatherApiDTO;
import model.Location;
import model.WeatherDay;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class WeatherMapper {

    private WeatherMapper() {
        // Utility class: non deve essere istanziata
    }

    public static WeatherDay toEntity(
            WeatherApiDTO dto,
            Location location
    ) {
        if (dto == null) {
            throw new IllegalArgumentException("WeatherApiDTO non può essere null");
        }

        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (dto.getDate() == null) {
            throw new IllegalArgumentException("La data del DTO meteo non può essere null");
        }

        /*
         * Ogni WeatherDay riceve l'orario esatto
         * del momento in cui viene creato/mappato.
         */
        LocalDateTime dateTime = dto.getDate().atTime(LocalTime.now());

        WeatherDay weatherDay = new WeatherDay();
        weatherDay.setLocation(location);
        weatherDay.setData(dateTime);
        weatherDay.setTempMin(dto.getTempMin());
        weatherDay.setTempMax(dto.getTempMax());
        weatherDay.setUmidita(dto.getUmidita());
        weatherDay.setPrecipitazione(dto.getPrecipitazione());
        weatherDay.setVentoKmh(dto.getVentoKmh());
        weatherDay.setUvIndex(dto.getUvIndex());

        return weatherDay;
    }
}