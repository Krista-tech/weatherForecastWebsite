package com.accenture.weatherForecastWebsite.version2.service;

import com.accenture.weatherForecastWebsite.version2.ApiService.WeatherAPIService;
import com.accenture.weatherForecastWebsite.version2.controller.ForecastRestController;
import com.accenture.weatherForecastWebsite.version2.converters.DateConverter;
import com.accenture.weatherForecastWebsite.version2.converters.TemperatureConverter;
import com.accenture.weatherForecastWebsite.version2.model.City;
import com.accenture.weatherForecastWebsite.version2.model.Forecast;
import com.accenture.weatherForecastWebsite.version2.repository.ForecastsByCityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Component
@Service
public class ForecastService {


    @Autowired
    City city;
    @Autowired
    Forecast forecastClass;

    @Autowired
    ForecastsByCityRepository forecastsByCityRepository;
    @Autowired
    WeatherAPIService weatherAPIService;
    @Autowired
    AddNewCity addNewCity;
    @Autowired
    UpdateCity updateCity;

    @Autowired
    DateConverter dateConverter;
    @Autowired
    TemperatureConverter temperatureConverter;
    @Autowired
    GetForecast getForecast;

    Logger serviceLogger = LoggerFactory.getLogger(ForecastRestController.class);

    public City getCity() {
        return city;
    }

    public Forecast findForecast(String cityName) {
        City matchedLocation = forecastsByCityRepository.findByCityNameIgnoreCaseContaining(cityName);


        if (matchedLocation != null) {
            serviceLogger.trace("Forecast for " + cityName + " found in database...");
            Timestamp lastTimeUpdate = matchedLocation.getTimestamp();
            Timestamp currentTimeMinusHour = new Timestamp((System.currentTimeMillis() - (60 * 60 * 1000)));
            serviceLogger.trace("Checking the timestamp...");

            if (lastTimeUpdate.after(currentTimeMinusHour)) {
                serviceLogger.trace("Data returned from database...");
                return getForecast.getForecast(matchedLocation);
            } else {
                serviceLogger.trace("Data retrieved from Api...");
                City forecast = weatherAPIService.getForecastByCity(cityName);

                return getForecast.getForecast(forecast);
            }

        } else {
            City forecast = weatherAPIService.getForecastByCity(cityName);
            serviceLogger.trace("Data retrieved from Api...");
            return getForecast.getForecast(forecast);
        }


    }

    public City setForecast(String cityName) {
        serviceLogger.trace("Posting data...");
        City matchedLocation = forecastsByCityRepository.findByCityNameIgnoreCaseContaining(cityName);

        if (matchedLocation == null) {
            serviceLogger.trace("No city found in database...");
            return addNewCity.addNewCiy(cityName);

        } else {
            serviceLogger.trace("Data about " + cityName + " already found in database");
            return updateCity.updateCity(matchedLocation);
        }

    }
}
