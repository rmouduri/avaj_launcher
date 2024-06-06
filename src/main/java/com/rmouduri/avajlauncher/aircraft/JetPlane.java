package src.main.java.com.rmouduri.avajlauncher.aircraft;

import java.io.IOException;
import java.util.Map;

import src.main.java.com.rmouduri.avajlauncher.app.Simulator;
import src.main.java.com.rmouduri.avajlauncher.tower.Tower;
import src.main.java.com.rmouduri.avajlauncher.tower.WeatherProvider;
import src.main.java.com.rmouduri.avajlauncher.tower.WeatherTower;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates.CoordinatesException;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates.HeightException;


/**
 * JetPlane
 */
public class JetPlane extends Aircraft {

    /**
     * Coordinates modifiers based on the weather
     */
    private static final Map<String, Map<Integer, Integer>> weatherConditionsCoordinatesModifier = Map.of(
        WeatherProvider.RAIN, Map.of(
            Coordinates.LONGITUDE, 0,
            Coordinates.LATITUDE, 5,
            Coordinates.HEIGHT, 0
        ),
        WeatherProvider.FOG, Map.of(
            Coordinates.LONGITUDE, 0,
            Coordinates.LATITUDE, 1,
            Coordinates.HEIGHT, 0
        ),
        WeatherProvider.SUN, Map.of(
            Coordinates.LONGITUDE, 0,
            Coordinates.LATITUDE, 10,
            Coordinates.HEIGHT, 2
        ),
        WeatherProvider.SNOW, Map.of(
            Coordinates.LONGITUDE, 0,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, -7
        )
    );

    /**
     * JetPlane messages based on the weather
     */
    private static final Map<String, String> weatherConditionsMessages = Map.of(
        WeatherProvider.RAIN, "It's raining. Better watch out for lightings.",
        WeatherProvider.FOG, "It's foggy. I can't see nothing.",
        WeatherProvider.SUN, "I'm burning!",
        WeatherProvider.SNOW, "Where Santa at? 🧑‍🎄"
    );

    /**
     * Aircraft type
     */
    protected static final String aircraftType = "JetPlane";

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
    public JetPlane(final long p_id, final String p_name, final Coordinates p_coordinates) {
        super(p_id, p_name, p_coordinates);
        this.outputName = String.format("%s#%s(%d)", JetPlane.aircraftType, this.name, this.id);
    }

    /**
     * Register @param p_tower as the WeatherTower of reference for the JetPlane
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
     * Updates then prints the current JetPlane condition
     */
    @Override
    public final void updateConditions() throws IOException, CoordinatesException {
        final String weather = this.weatherTower.getWeather(this.coordinates);

        Simulator.fileWriter.write(String.format("%s: %s\n", this.outputName, JetPlane.weatherConditionsMessages.get(weather)));

        try {
            this.coordinates = new Coordinates(
                this.coordinates.getLongitude() + JetPlane.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.LONGITUDE),
                this.coordinates.getLatitude() + JetPlane.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.LATITUDE),
                this.coordinates.getHeight() + JetPlane.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.HEIGHT)
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