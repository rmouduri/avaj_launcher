package src.main.java.com.rmouduri.avajlauncher.tower;

import java.io.IOException;

import src.main.java.com.rmouduri.avajlauncher.util.Coordinates;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates.CoordinatesException;


/**
 * WeatherTower
 */
public class WeatherTower extends Tower {

    /**
     * Get the current weather at the @param p_coordinates from Tower superclass
     * 
     * @param p_coordinates
     * @return The current weather at @param p_coordinates
     */
    public String getWeather(Coordinates p_coordinates) {
        return WeatherProvider.getCurrentWeather(p_coordinates);
    }

    /**
     * Update
     */
    public void changeWeather() throws IOException, CoordinatesException {
        this.conditionChanged();
    }
}