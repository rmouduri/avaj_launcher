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
 * Helicopter
 */
public class Helicopter extends Aircraft {

    /**
     * Coordinates modifiers based on the weather
     */
    private static final Map<String, Map<Integer, Integer>> weatherConditionsCoordinatesModifier = Map.of(
        WeatherProvider.RAIN, Map.of(
            Coordinates.LONGITUDE, 5,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, 0
        ),
        WeatherProvider.FOG, Map.of(
            Coordinates.LONGITUDE, 1,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, 0
        ),
        WeatherProvider.SUN, Map.of(
            Coordinates.LONGITUDE, 10,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, 2
        ),
        WeatherProvider.SNOW, Map.of(
            Coordinates.LONGITUDE, 0,
            Coordinates.LATITUDE, 0,
            Coordinates.HEIGHT, -12
        )
    );

    /**
     * Helicopter messages based on the weather
     */
    private static final Map<String, String> weatherConditionsMessages = Map.of(
        WeatherProvider.RAIN, "Why can't I have windscreen wipers on that big ah window",
        WeatherProvider.FOG, "My rotors clear the fog, it's pretty useful",
        WeatherProvider.SUN, "The rotors are giving me some air.",
        WeatherProvider.SNOW, "My rotors froze, I'm gon die 🫠"
    );

    /**
     * Aircraft type
     */
    protected static final String aircraftType = "Helicopter";

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
    public Helicopter(final long p_id, final String p_name, final Coordinates p_coordinates) {
        super(p_id, p_name, p_coordinates);
        this.outputName = String.format("%s#%s(%d)", Helicopter.aircraftType, this.name, this.id);
    }

    /**
     * Register @param p_tower as the WeatherTower of reference for the Helicopter
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
     * Updates then prints the current Helicopter condition
     */
    @Override
    public final void updateConditions() throws IOException, CoordinatesException {
        final String weather = this.weatherTower.getWeather(this.coordinates);

        Simulator.fileWriter.write(String.format("%s: %s\n", this.outputName, Helicopter.weatherConditionsMessages.get(weather)));

        try {
            this.coordinates = new Coordinates(
                this.coordinates.getLongitude() + Helicopter.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.LONGITUDE),
                this.coordinates.getLatitude() + Helicopter.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.LATITUDE),
                this.coordinates.getHeight() + Helicopter.weatherConditionsCoordinatesModifier.get(weather).get(Coordinates.HEIGHT)
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