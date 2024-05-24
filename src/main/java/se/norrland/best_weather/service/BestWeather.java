/**
 * Represents the best weather data from various sources.
 */
package se.norrland.best_weather.service;

public class BestWeather {
    private String origin;
    private Double temp;
    private Double humidity;
    private String timestamp;

    // Getters and setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" +
                "origin='" + origin + '\'' +
                ", temp=" + temp +
                ", humidity=" + humidity +
                " % , timestamp='" + timestamp + '\'' +
                '}';
    }
}