/**
 * Client for retrieving weather data from the Met Norway API.
 */
package se.norrland.best_weather.clients.met;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.norrland.best_weather.clients.met.model.Details;
import se.norrland.best_weather.clients.met.model.Met;
import se.norrland.best_weather.clients.met.model.Timeseries;
import se.norrland.best_weather.clients.met.model.Units;
import se.norrland.best_weather.service.BestWeather;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

@Component
public class MetClient {

    private static final Logger logger = LoggerFactory.getLogger(MetClient.class);

    private static final String GET_URI = "https://api.met.no/weatherapi/locationforecast/2.0/" +
            "compact?lat=59.3110&lon=18.0300";

    private final WebClient client;

    @Autowired
    public MetClient(WebClient.Builder webClientBuilder) {
        this.client = webClientBuilder
                .baseUrl(GET_URI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public MetClient(WebClient client) {
        this.client = client;
    }

    public BestWeather getMetData() {
        logger.info("Fetching weather data from Met Norway API");

        Mono<Met> mono = client
                .get()
                .uri(GET_URI)
                .retrieve()
                .bodyToMono(Met.class);

        Met met = mono.block();
        if (met == null) {
            logger.error("Failed to retrieve weather data from Met Norway API");
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in24Hours = now.plusHours(24);

        Timeseries closestTimeSeries = null;
        long minDifference = Long.MAX_VALUE;

        for (Timeseries t : met.getProperties().getTimeseries()) {
            try {
                LocalDateTime time = LocalDateTime.parse(t.getTime(), java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME);
                long difference = Math.abs(time.until(in24Hours, ChronoUnit.SECONDS));
                if (difference < minDifference) {
                    minDifference = difference;
                    closestTimeSeries = t;
                }
            } catch (DateTimeParseException e) {
                logger.error("Error parsing date time: {}", t.getTime(), e);
            }
        }

        if (closestTimeSeries == null) {
            logger.warn("No measurement found close to 24 hours from now.");
            return null;
        }

        double temp = closestTimeSeries.getData().getInstant().getDetails().getAirTemperature();
        double humidity = closestTimeSeries.getData().getInstant().getDetails().getRelativeHumidity();

        BestWeather metData = new BestWeather();
        LocalDateTime time = LocalDateTime.parse(closestTimeSeries.getTime(), java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME);

        metData.setHumidity(humidity);
        metData.setTimestamp(time.toString());
        metData.setTemp(temp);
        metData.setOrigin("Met Norway");

        logger.info("Weather data retrieved successfully from Met Norway: {}", metData);

        return metData;
    }

}
