package service;

import jakarta.persistence.EntityManager;
import model.WeatherDay;
import repository.WeatherDayRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class WeatherService {

    private final WeatherDayRepository weatherDayRepository;

    public WeatherService(EntityManager em) {
        this.weatherDayRepository = new WeatherDayRepository(em);
    }

    public WeatherDay salvaWeatherDay(WeatherDay weatherDay) {
        validateWeatherDay(weatherDay);

        LocalDate forecastDate = weatherDay.getData().toLocalDate();

        LocalDateTime startOfDay = forecastDate.atStartOfDay();
        LocalDateTime startOfNextDay = forecastDate.plusDays(1).atStartOfDay();

        Optional<WeatherDay> existing =
                weatherDayRepository.findByLocationAndDay(
                        weatherDay.getLocation(),
                        startOfDay,
                        startOfNextDay
                );

        if (existing.isPresent()) {
            return existing.get();
        }

        return weatherDayRepository.save(weatherDay);
    }



    private void validateWeatherDay(WeatherDay weatherDay) {
        if (weatherDay == null) {
            throw new IllegalArgumentException("WeatherDay non può essere null");
        }

        if (weatherDay.getLocation() == null) {
            throw new IllegalArgumentException("WeatherDay deve avere una Location associata");
        }

        if (weatherDay.getData() == null) {
            throw new IllegalArgumentException("WeatherDay deve avere una data");
        }
    }
}