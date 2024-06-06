package src.main.java.com.rmouduri.avajlauncher.aircraft;

import java.util.Map;

import src.main.java.com.rmouduri.avajlauncher.app.Simulator;
import src.main.java.com.rmouduri.avajlauncher.tower.Tower;
import src.main.java.com.rmouduri.avajlauncher.tower.WeatherProvider;
import src.main.java.com.rmouduri.avajlauncher.tower.WeatherTower;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates.CoordinatesException;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates.HeightException;

import java.io.IOException;


/**
 * Baloon
 */
public class Baloon extends Aircraft {

    /**
     * Coordinates modifiers based on the weather
     */
    private static final Map<String, Map<Integer, Integer>> weatherConditionsCoordinatesModifier = Map.of(
        WeatherProvider.RAIN, Map.of(
            Coordinates.LONGITUDE, 0,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, -5
        ),
        WeatherProvider.FOG, Map.of(
            Coordinates.LONGITUDE, 0,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, -3
        ),
        WeatherProvider.SUN, Map.of(
            Coordinates.LONGITUDE, 2,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, 4
        ),
        WeatherProvider.SNOW, Map.of(
            Coordinates.LONGITUDE, 0,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, -15
        )
    );

    /**
     * Baloon messages based on the weather
     */
    private static final Map<String, String> weatherConditionsMessages = Map.of(
        WeatherProvider.RAIN, "Can the rain pierce my baloon?",
        WeatherProvider.FOG, "I can't see nothing, a plane gon destroy me baloon 💀",
        WeatherProvider.SUN, "IS MY BALOON GONNA EXPLODE WITH THIS HEAT!?",
        WeatherProvider.SNOW, "My baloon gon freeze bruh."
    );

    /**
     * Aircraft type
     */
    protected static final String aircraftType = "Baloon";

    /**
     * Name outputted before a message
     */
    private String outputName;


    /**
     * Constructor
     * 
     * @param p_name
     * @param p_coordinates
     */
    public Baloon(final long p_id, final String p_name, final Coordinates p_coordinates) {
        super(p_id, p_name, p_coordinates);
        this.outputName = String.format("%s#%s(%d)", Baloon.aircraftType, this.name, this.id);
    }

    /**
     * Register @param p_tower as the WeatherTower of reference for the Baloon
     * 
     * @param p_tower
     */
    @Override
    public void registerTower(WeatherTower p_tower) throws IOException {
        super.registerTower(p_tower);

        Simulator.fileWriter.write(String.format("%s says: %s registered to weather tower.\n", Tower.outputName, this.outputName));
    };

    /**
     * Land and Unregister from weatherTower
     */
    private void land() throws IOException {
        Simulator.fileWriter.write(String.format("%s landing.\n", this.outputName));
        this.weatherTower.unregister(this);
        Simulator.fileWriter.write(String.format("%s says: %s unregistered from weather tower.\n", Tower.outputName, this.outputName));
    }

    /**
     * Updates then prints the current Baloon condition
     */
    @Override
    public final void updateConditions() throws IOException, CoordinatesException {
        final String weather = this.weatherTower.getWeather(this.coordinates);

        Simulator.fileWriter.write(String.format("%s: %s\n", this.outputName, Baloon.weatherConditionsMessages.get(weather)));

        try {
            this.coordinates = new Coordinates(
                this.coordinates.getLongitude() + Baloon.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.LONGITUDE),
                this.coordinates.getLatitude() + Baloon.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.LATITUDE),
                this.coordinates.getHeight() + Baloon.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.HEIGHT)
            );
        } catch (CoordinatesException e) {
            if (e instanceof HeightException) {
                this.land();
                return ;
            } else { // Should never happen
                throw e;
            }
        }
    }
}