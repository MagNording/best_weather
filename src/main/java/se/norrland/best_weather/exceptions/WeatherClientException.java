package se.norrland.best_weather.exceptions;

/**
 * Exception thrown when there is an issue with a weather client.
 */
public class WeatherClientException extends RuntimeException {
    public WeatherClientException(String message) {
        super(message);
    }
}
