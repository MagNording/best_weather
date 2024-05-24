/**
 * Client for retrieving weather data from the Meteo API.
 */
package se.norrland.best_weather.clients.meteo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.norrland.best_weather.clients.met.MetClient;
import se.norrland.best_weather.clients.meteo.model.Meteo;
import se.norrland.best_weather.clients.smhi.model.TimeSeries;
import se.norrland.best_weather.service.BestWeather;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class MeteoClient {

    private static final Logger logger = LoggerFactory.getLogger(MetClient.class);

    private static final String GET_URI = "https://api.open-meteo.com/v1/forecast?" +
            "latitude=59.3094&longitude=18.0234&hourly=temperature_2m,relative_humidity_2m&forecast_days=3";

    private final WebClient client;

    @Autowired
    public MeteoClient(WebClient.Builder webClientBuilder) {
        this.client = webClientBuilder
                .baseUrl(GET_URI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public BestWeather getMeteoData() {
        logger.info("Fetching weather data from Meteo API");

        Mono<Meteo> mono = client
                .get()
                .uri(GET_URI)
                .retrieve()
                .bodyToMono(Meteo.class);

        Meteo meteo = mono.block();
        if (meteo == null) {
            logger.error("Failed to retrieve weather data from Meteo API");
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in24Hours = now.plusHours(24);
        double temperature = 0;

        BestWeather meteoData = new BestWeather();

        if (meteo != null && meteo.getHourly() != null) {

            List<String> times = meteo.getHourly().getTime();
            List<Integer> temperatures = meteo.getHourly().getTemperature2m();
            List<Integer> humidities = meteo.getHourly().getRelativeHumidity2m();

            if (times != null && !times.isEmpty() && temperatures != null && !temperatures.isEmpty() && humidities != null && !humidities.isEmpty()) {

                for (int i = 0; i < times.size(); i++) {
                    LocalDateTime time = LocalDateTime.parse(times.get(i), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                    if (!time.isBefore(in24Hours)) {
                        temperature = (double) temperatures.get(i);
                        meteoData.setTimestamp(in24Hours.toString());
                        meteoData.setHumidity((double) humidities.get(i));
                        meteoData.setTemp(temperature);
                        meteoData.setOrigin("Meteo");
                        break;
                    }
                }
            } else {
                logger.warn("No current weather data available.");
            }
        } else {
            logger.warn("No current weather data available.");
        }
        logger.info("Weather data retrieved successfully from Meteo: {}", meteoData);

        return meteoData;
    }
}
