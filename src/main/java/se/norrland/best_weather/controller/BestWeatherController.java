/**
 * Controller for handling requests related to the best weather data.
 */
package se.norrland.best_weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.norrland.best_weather.service.BestWeather;
import se.norrland.best_weather.service.BestWeatherService;

@Controller
public class BestWeatherController {

    private final BestWeatherService bestWeatherService;

    public BestWeatherController(BestWeatherService bestWeatherService) {
        this.bestWeatherService = bestWeatherService;
    }

    @GetMapping("/best-weather")
    public String getBestWeather(Model model) {
        BestWeather bestWeather = bestWeatherService.getBestWeather();
        model.addAttribute("origin", bestWeather.getOrigin());
        model.addAttribute("time", bestWeather.getTimestamp());
        model.addAttribute("temp", bestWeather.getTemp());
        model.addAttribute("humidity", bestWeather.getHumidity());
        return "weather";
    }
}
