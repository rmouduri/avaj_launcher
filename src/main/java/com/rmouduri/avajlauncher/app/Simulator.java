package src.main.java.com.rmouduri.avajlauncher.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.NoSuchElementException;
import java.util.Scanner;

import src.main.java.com.rmouduri.avajlauncher.aircraft.AircraftFactory;
import src.main.java.com.rmouduri.avajlauncher.exception.AvajLauncherException;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates.CoordinatesException;
import src.main.java.com.rmouduri.avajlauncher.util.Coordinates;
import src.main.java.com.rmouduri.avajlauncher.tower.WeatherTower;


/**
 * Simulator
 */
public class Simulator {

    private static class SimulationException extends AvajLauncherException {
        private SimulationException() {
            super();
        }
    
        private SimulationException(String message) {
            super(message);
        }
    
        private SimulationException(Throwable cause) {
            super(cause);
        }
    }


    /**
     * FileWriter 
     */
    public static FileWriter fileWriter;

    /**
     * The weather tower used by all Flyable
     */
    private static final WeatherTower weatherTower = new WeatherTower();

    /**
     * Amount of Simulation loops i.e. amount of times weatherTower.changeWeather() will be called
     */
    private static AtomicInteger simulationLoops;
    

    /**
     * Runs the Simulation
     * 
     * @throws SimulationException
     */
    private static final void runSimulation() throws SimulationException {
        while (simulationLoops.getAndDecrement() > 0) {
            try {
                weatherTower.changeWeather();
            } catch (IOException e) {
                throw new SimulationException(e);
            } catch (CoordinatesException e) { // Should never happen
                throw new SimulationException(e);
            }
        }
    }

    /**
     * Creates the file that will store the logs
     * 
     * @throws SimulationException
     */
    private static void createFileWriter() throws SimulationException {
        File file = new File("simulation.txt");

        try {
            Simulator.fileWriter = new FileWriter(file);
        }
         catch (IOException e) {
            throw new SimulationException(e.getMessage());
        }
    }

    /**
     * Opens, reads and parses the file in @param file then creates new Aircrafts
     * 
     * @param file
     * @throws SimulationException
     */
    private static void parse(final String filePath) throws SimulationException {
        File file;
        Scanner scanner;

        try {
            file = new File(filePath);
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new SimulationException(e.getMessage());
        }

        try {
            simulationLoops = new AtomicInteger(Integer.parseInt(scanner.nextLine()));
        } catch (NoSuchElementException e) {
            scanner.close();
            throw new SimulationException("File is empty.");
        } catch (NumberFormatException e) {
            scanner.close();
            throw new SimulationException("First line should be an Integer.");
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parsedLine = line.split(" ");

            if (parsedLine.length != 5) {
                scanner.close();
                throw new SimulationException(String.format("Invalid scenario line `%s`", line));
            }

            try {
                AircraftFactory.newAircraft(parsedLine[0], parsedLine[1],
                        new Coordinates(Integer.parseInt(parsedLine[2]), Integer.parseInt(parsedLine[3]), Integer.parseInt(parsedLine[4])))
                    .registerTower(weatherTower);
            } catch (NumberFormatException e) {
                scanner.close();
                throw new SimulationException(String.format("Invalid coordinates: `%s`", line));
            } catch (IOException e) {
                scanner.close();                
                throw new SimulationException(e.getMessage());
            } catch (AvajLauncherException e) {
                scanner.close();
                throw new SimulationException(e.getMessage());
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("One and only one argument [file] is required.");
            return ;
        }

        try {
            createFileWriter();
            parse(args[0]);
            runSimulation();
        } catch (SimulationException e) {
            System.out.println(String.format("Simulation Exception Caught: %s", e.getMessage()));
            return ;
        }

        try {
            Simulator.fileWriter.close();
        } catch (IOException e) {
            System.out.println(String.format("IOException Caught when closing Simulator.FileWriter: %s", e.getMessage()));
        }
    }
}