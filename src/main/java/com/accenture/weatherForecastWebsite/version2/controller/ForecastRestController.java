package com.accenture.weatherForecastWebsite.version2.controller;

import com.accenture.weatherForecastWebsite.version2.logic.CityService;
import com.accenture.weatherForecastWebsite.version2.model.City;
import com.accenture.weatherForecastWebsite.version2.model.Forecast;
import com.accenture.weatherForecastWebsite.version2.repository.ForecastsByCityRepository;
import com.accenture.weatherForecastWebsite.version2.logic.ForecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.accenture.weatherForecastWebsite.version2.constants.ForecastConstants.API_MEDIA_TYPE;


@RestController
@RequestMapping("/forecast")
public class ForecastRestController {


    @Autowired
    ForecastService forecastService;

    @Autowired
    ForecastsByCityRepository forecastsByCityRepository;

    @Autowired
    CityService cityService;




    @GetMapping(value = "/{cityName}", produces = API_MEDIA_TYPE)
    public Forecast getForecast(@PathVariable String cityName) {
        return forecastService.findForecast(cityName);
    }


    @PostMapping(value = "/{cityName}")
    public City setForecast(@PathVariable String cityName) {
        return forecastService.setForecast(cityName);
    }

    @PutMapping(value = "/{cityId}")
    public City updatedForecast(@PathVariable String cityId) {

        return cityService.updateCity(forecastsByCityRepository.findById(cityId).get());

    }
}
