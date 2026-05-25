package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto_mapper.WeatherApiDTO;
import dto_mapper.WeatherMapper;
import model.Location;
import model.WeatherDay;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Servizio per l'integrazione con API meteo esterne.
 *
 * Responsabilità:
 * - validare input location/giorni
 * - costruire URL per Open-Meteo
 * - chiamare API esterna
 * - convertire JSON in WeatherApiDTO
 * - convertire DTO in WeatherDay tramite mapper
 *
 * WeatherApiDTO = dato esterno/API
 * WeatherDay = entity interna/JPA
 */
public class ExternalWeatherService {

    private static final String OPEN_METEO_BASE_URL =
            "https://api.open-meteo.com/v1/forecast";

    public WeatherDay fetchTodayWeather(Location location) {
        List<WeatherDay> forecast = fetchForecast(location, 2);

        if (forecast.isEmpty()) {
            throw new RuntimeException("Nessun dato meteo ricevuto");
        }

        return forecast.get(0);
    }

    public List<WeatherDay> fetchForecast(Location location, int days) {

        validateInput(location, days);

        String url = buildUrl(location, days);

        try {
            String responseBody = sendRequest(url);

            List<WeatherApiDTO> dtoList = parseForecastToDTO(responseBody);

            /*
             * Ogni DTO viene convertito in WeatherDay separatamente.
             * Il mapper assegna LocalTime.now() al singolo WeatherDay.
             */
            return dtoList.stream()
                    .map(dto -> WeatherMapper.toEntity(dto, location))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Errore I/O durante la chiamata API meteo", e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Chiamata API meteo interrotta", e);
        }
    }

    private void validateInput(Location location, int days) {
        if (location == null) {
            throw new IllegalArgumentException("Location non può essere null");
        }

        if (location.getLatitudine() == null || location.getLongitudine() == null) {
            throw new IllegalArgumentException("Coordinate non valide");
        }

        if (days <= 0 || days > 16) {
            throw new IllegalArgumentException("Numero giorni non valido (1-16)");
        }
    }

    private String buildUrl(Location location, int days) {
        return String.format(
                Locale.US,
                "%s?latitude=%f&longitude=%f" +
                        "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,wind_speed_10m_max,uv_index_max" +
                        "&hourly=relative_humidity_2m" +
                        "&forecast_days=%d" +
                        "&timezone=auto",
                OPEN_METEO_BASE_URL,
                location.getLatitudine(),
                location.getLongitudine(),
                days
        );
    }

    private String sendRequest(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException(
                    "Errore API meteo - status: " + response.statusCode()
            );
        }

        return response.body();
    }

    private List<WeatherApiDTO> parseForecastToDTO(String json) {
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("JSON meteo vuoto o null");
        }

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        JsonObject daily = root.getAsJsonObject("daily");

        if (daily == null) {
            throw new RuntimeException("Risposta API non valida: sezione daily mancante");
        }

        JsonArray dates = daily.getAsJsonArray("time");
        JsonArray tempMaxArray = daily.getAsJsonArray("temperature_2m_max");
        JsonArray tempMinArray = daily.getAsJsonArray("temperature_2m_min");
        JsonArray precipitationArray = daily.getAsJsonArray("precipitation_sum");
        JsonArray windArray = daily.getAsJsonArray("wind_speed_10m_max");
        JsonArray uvArray = daily.getAsJsonArray("uv_index_max");

        if (dates == null) {
            throw new RuntimeException("Risposta API non valida: array time mancante");
        }

        List<WeatherApiDTO> result = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            String dateString = dates.get(i).getAsString();

            LocalDate date = LocalDate.parse(dateString);

            Double tempMax = getDouble(tempMaxArray, i);
            Double tempMin = getDouble(tempMinArray, i);
            Double precipitation = getDouble(precipitationArray, i);
            Double wind = getDouble(windArray, i);
            Double uv = getDouble(uvArray, i);
            Double humidity = calculateAverageHumidity(root, dateString);

            WeatherApiDTO dto = new WeatherApiDTO();
            dto.setDate(date);
            dto.setTempMax(tempMax);
            dto.setTempMin(tempMin);
            dto.setPrecipitazione(precipitation);
            dto.setVentoKmh(wind);
            dto.setUvIndex(uv);
            dto.setUmidita(humidity);

            result.add(dto);
        }

        return result;
    }

    private Double getDouble(JsonArray array, int index) {
        if (array == null || index >= array.size() || array.get(index).isJsonNull()) {
            return null;
        }

        return array.get(index).getAsDouble();
    }

    private Double calculateAverageHumidity(JsonObject root, String date) {
        if (!root.has("hourly")) {
            return null;
        }

        JsonObject hourly = root.getAsJsonObject("hourly");

        if (!hourly.has("time") || !hourly.has("relative_humidity_2m")) {
            return null;
        }

        JsonArray times = hourly.getAsJsonArray("time");
        JsonArray humidityValues = hourly.getAsJsonArray("relative_humidity_2m");

        double sum = 0.0;
        int count = 0;

        for (int i = 0; i < times.size(); i++) {
            String time = times.get(i).getAsString();

            if (time.startsWith(date) && !humidityValues.get(i).isJsonNull()) {
                sum += humidityValues.get(i).getAsDouble();
                count++;
            }
        }

        return count == 0 ? null : sum / count;
    }
}
