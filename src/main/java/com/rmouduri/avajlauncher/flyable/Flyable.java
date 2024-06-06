package src.main.java.com.rmouduri.avajlauncher.flyable;

import java.io.IOException;

import src.main.java.com.rmouduri.avajlauncher.tower.WeatherTower;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates.CoordinatesException;


public interface Flyable {

    public void updateConditions() throws IOException, CoordinatesException;

    public void registerTower(WeatherTower p_tower) throws IOException;
}