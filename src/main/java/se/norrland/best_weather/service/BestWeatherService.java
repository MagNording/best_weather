/**
 * Service for retrieving the best weather data from multiple sources.
 */
package se.norrland.best_weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.norrland.best_weather.clients.met.MetClient;
import se.norrland.best_weather.clients.meteo.MeteoClient;
import se.norrland.best_weather.clients.smhi.SmhiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.norrland.best_weather.exceptions.NoWeatherDataException;
import se.norrland.best_weather.exceptions.WeatherClientException;

import java.util.Comparator;
import java.util.List;

@Service
public class BestWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(BestWeatherService.class);

    private final SmhiClient smhiClient;
    private final MetClient metClient;
    private final MeteoClient meteoClient;

    /**
     * Constructs a new BestWeatherService with the given clients.
     *
     * @param smhiClient the client for SMHI data
     * @param metClient the client for Met data
     * @param meteoClient the client for Meteo data
     */
    @Autowired
    public BestWeatherService(SmhiClient smhiClient, MetClient metClient, MeteoClient meteoClient) {
        this.smhiClient = smhiClient;
        this.metClient = metClient;
        this.meteoClient = meteoClient;
    }
    /**
     * Retrieves the best weather data from the available clients.
     *
     * @return the best weather data
     * @throws NoWeatherDataException if no weather data is available
     */
    public BestWeather getBestWeather() {
        try {
            List<BestWeather> weatherDataList = List.of(
                    smhiClient.getSmhiData(),
                    metClient.getMetData(),
                    meteoClient.getMeteoData()
            );

            BestWeather bestWeatherData = weatherDataList.stream()
                    .max(Comparator.comparingDouble(BestWeather::getTemp))
                    .orElseThrow(() -> {
                        logger.error("No weather data available");
                        return new NoWeatherDataException("No weather data available");
                    });

            logger.info("Best weather data retrieved: {}", bestWeatherData);
            return bestWeatherData;
        } catch (Exception ex) {
            logger.error("Error retrieving weather data", ex);
            throw new WeatherClientException("Failed to retrieve weather data from one or more clients");
        }
    }
}
