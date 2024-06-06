package src.main.java.com.rmouduri.avajlauncher.aircraft;

import java.io.IOException;

import src.main.java.com.rmouduri.avajlauncher.flyable.Flyable;
import src.main.java.com.rmouduri.avajlauncher.tower.WeatherTower;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates;


/**
 * Aircraft
 */
public abstract class Aircraft implements Flyable {

    /**
     * Aircraft id
     */
    protected long id;
    
    /**
     * Aircraft name
     */
    protected String name;
    
    /**
     * Aircraft coordinates
     */
    protected Coordinates coordinates;

    /**
     * Aircraft weatherTower of reference
     */
    protected WeatherTower weatherTower;


    /**
     * Constructor
     * 
     * @param p_name
     * @param p_coordinate
     */
    protected Aircraft(final long p_id, final String p_name, final Coordinates p_coordinate) {
        this.id = p_id;
        this.name = p_name;
        this.coordinates = p_coordinate;
    }

    /**
     * Register @param p_tower as the WeatherTower of reference for the Aircraft
     * 
     * @param p_tower
     */
    @Override
    public void registerTower(WeatherTower p_tower) throws IOException {
        this.weatherTower = p_tower;
        this.weatherTower.register(this);
    };
}