/**
 * REST controller for handling requests related to the best weather data.
 */
package se.norrland.best_weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.norrland.best_weather.service.BestWeather;
import se.norrland.best_weather.service.BestWeatherService;

@RestController
@RequestMapping("/weather")
public class BestWeatherRESTController {

    private final BestWeatherService bestWeatherService;

    public BestWeatherRESTController(BestWeatherService bestWeatherService) {
        this.bestWeatherService = bestWeatherService;
    }

    @GetMapping("/liljeholmen")
    public BestWeather getBestWeather() {
        return bestWeatherService.getBestWeather();
    }
}
