package src.main.java.com.rmouduri.avajlauncher.aircraft;

import java.util.concurrent.atomic.AtomicLong;

import src.main.java.com.rmouduri.avajlauncher.exception.AvajLauncherException;
import src.main.java.com.rmouduri.avajlauncher.flyable.Flyable;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates;


/**
 * AircraftFactory
 */
public class AircraftFactory {

    public static class AircraftTypeException extends AvajLauncherException {
        public AircraftTypeException() {
            super();
        }
    
        public AircraftTypeException(String message) {
            super(message);
        }
    
        public AircraftTypeException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * Aircraft id generator
     */
    private static AtomicLong idGenerator = new AtomicLong(1);


    /**
     * Generates a new Flyable of type @param p_type
     * 
     * @param p_type
     * @param p_name
     * @param p_coordinates
     * @return The new Flyable of type @param p_type
     */
    public static Flyable newAircraft(String p_type, String p_name, Coordinates p_coordinates) throws AircraftTypeException{
        switch (p_type) {
            case Baloon.aircraftType:
                return new Baloon(AircraftFactory.idGenerator.getAndIncrement(), p_name, p_coordinates);
            case Helicopter.aircraftType:
                return new Helicopter(AircraftFactory.idGenerator.getAndIncrement(), p_name, p_coordinates);
            case JetPlane.aircraftType:
                return new JetPlane(AircraftFactory.idGenerator.getAndIncrement(), p_name, p_coordinates);
            default:
                throw new AircraftTypeException(String.format("Invalid Aircraft type: `%s`", p_type));
        }
    }
}