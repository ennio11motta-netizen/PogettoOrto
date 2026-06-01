
package service;

import model.WeatherDay;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.WeatherDayRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherDayRepository weatherDayRepository;

    public WeatherService(WeatherDayRepository weatherDayRepository) {
        if (weatherDayRepository == null) {
            throw new IllegalArgumentException("WeatherDayRepository non può essere null");
        }

        this.weatherDayRepository = weatherDayRepository;
    }
    @Transactional
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