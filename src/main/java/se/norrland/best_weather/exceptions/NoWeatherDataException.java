package se.norrland.best_weather.exceptions;

/**
 * Exception thrown when no weather data is available.
 */
public class NoWeatherDataException extends RuntimeException {
    public NoWeatherDataException(String message) {
        super(message);
    }
}
