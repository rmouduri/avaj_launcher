package src.main.java.com.rmouduri.avajlauncher.util;

import src.main.java.com.rmouduri.avajlauncher.exception.AvajLauncherException;


/**
 * Coordinates
 */
public class Coordinates {

    public abstract static class CoordinatesException extends AvajLauncherException {

        public CoordinatesException() {
            super();
        }
    
        public CoordinatesException(String message) {
            super(message);
        }
    
        public CoordinatesException(Throwable cause) {
            super(cause);
        }
    }

    public static class LongitudeException extends CoordinatesException {
        public LongitudeException() {
            super();
        }
    
        public LongitudeException(String message) {
            super(message);
        }
    
        public LongitudeException(Throwable cause) {
            super(cause);
        }
    }

    public static class LatitudeException extends CoordinatesException {
        public LatitudeException() {
            super();
        }
    
        public LatitudeException(String message) {
            super(message);
        }
    
        public LatitudeException(Throwable cause) {
            super(cause);
        }
    }

    public static class HeightException extends CoordinatesException {
        public HeightException() {
            super();
        }
    
        public HeightException(String message) {
            super(message);
        }
    
        public HeightException(Throwable cause) {
            super(cause);
        }
    }


    /**
     * The 3 coordinates
     */
    private int longitude;
    private int latitude;
    private int height;

    /**
     * Index of coordinates for debugging purposes
     */
    public static final int LONGITUDE = 0;
    public static final int LATITUDE = 1;
    public static final int HEIGHT = 2;


    /**
     * Constructor
     * 
     * @param p_longitude
     * @param p_latitude
     * @param p_height
     */
    public Coordinates(int p_longitude, int p_latitude, int p_height) throws CoordinatesException {
        if (p_longitude <= 0) {
            throw new LongitudeException(String.format("Longitude must be positive: `%d`", p_longitude));
        } else if (p_latitude <= 0) {
            throw new LatitudeException(String.format("Latitude must be positive: `%d`", p_latitude));
        } else if (p_height <= 0) {
            throw new HeightException(String.format("Height must be positive: `%d`", p_height));
        }

        if (p_height > 100) {
            p_height = 100;
        }

        this.longitude = p_longitude;
        this.latitude = p_latitude;
        this.height = p_height;
    }

    /**
     * Get the longitude
     * 
     * @return The longitude
     */
    public final int getLongitude() {
        return this.longitude;
    }

    /**
     * Get the latitude
     * 
     * @return The latitude
     */
    public final int getLatitude() {
        return this.latitude;
    }

    /**
     * Get the height
     * 
     * @return The height
     */
    public final int getHeight() {
        return this.height;
    }
}