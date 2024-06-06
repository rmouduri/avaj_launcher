package src.main.java.com.rmouduri.avajlauncher.tower;

import src.main.java.com.rmouduri.avajlauncher.util.Coordinates;


/**
 * WeatherProvider
 */
public class WeatherProvider {

    public static final String RAIN = "Rain";
    public static final String FOG = "Fog";
    public static final String SUN = "Sun";
    public static final String SNOW = "Snow";

    /**
     * The 4 weather types
     */
    private static final String[] weather = {RAIN, FOG, SUN, SNOW};


    /**
     * Constructor
     */
    private WeatherProvider() {};

    /**
     * Return a weather type based on the coordinates in @param p_coordinates
     * 
     * @param p_coordinates
     * @return The weather type
     */
    public static String getCurrentWeather(Coordinates p_coordinates) {
        return weather[(p_coordinates.getLongitude() + p_coordinates.getLatitude() + p_coordinates.getHeight()) % 4];
    }
}