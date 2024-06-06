package src.main.java.com.rmouduri.avajlauncher.tower;

import java.io.IOException;
import java.util.ArrayList;

import src.main.java.com.rmouduri.avajlauncher.flyable.Flyable;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates.CoordinatesException;


/**
 * Tower
 */
public class Tower {

    /**
     * List of Flyable observed
     */
    private ArrayList<Flyable> observers = new ArrayList<Flyable>();

    public static final String outputName = "Tower";


    /**
     * Register @param p_flyable to the observers List of Flyable
     * 
     * @param p_flyable
     */
    public void register(Flyable p_flyable) {
        this.observers.add(p_flyable);
    }

    /**
     * Unregister @param p_flyable from the observers List of Flyable
     * 
     * @param p_flyable
     */
    public void unregister(Flyable p_flyable) {
        this.observers.remove(p_flyable);
    }

    /**
     * Update condition of every Flyable in observers
     */
    protected void conditionChanged() throws IOException, CoordinatesException {
        int i = 0;
        int arraySize = this.observers.size();

        while (i < arraySize) {
            this.observers.get(i).updateConditions();

            /**
             * In case of when Flying object removes itself from observers in updateConditions() when height becomes < 0
             * We decrement arraySize and don't increment i
             */
            if (this.observers.size() < arraySize) {
                arraySize--;
            } else {
                i++;
            }
        }
    }
}