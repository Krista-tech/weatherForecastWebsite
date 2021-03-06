package com.accenture.weatherForecastWebsite.version2.service;

import com.accenture.weatherForecastWebsite.version2.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URL;

import static com.accenture.weatherForecastWebsite.version2.constants.ForecastConstants.*;

@CacheConfig(cacheNames = "cityCache")
@Service
public class WeatherAPIRequests {

    @Value("${weather.api.key}") //You have to have API key to access external API
    private String apiKey;

    @Autowired
    WeatherApiWebClient weatherApiWebClient;

    @Cacheable(cacheNames = "findByCityName", key = "#userInput")
    public City getForecastByCity(String userInput) {


        try {
            URL url = new URL(OPENWEATHER_API_BASE_URL + REQUEST_BY_NAME + userInput + apiKey);
            return weatherApiWebClient.getCityInformation(url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Cacheable(cacheNames = "findByCityId", key = "#cityID")
    public City getForecastByCityID(String cityID) {

        try {
            URL url = new URL(OPENWEATHER_API_BASE_URL + REQUEST_BY_ID + cityID + apiKey);
            return weatherApiWebClient.getCityInformation(url);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
