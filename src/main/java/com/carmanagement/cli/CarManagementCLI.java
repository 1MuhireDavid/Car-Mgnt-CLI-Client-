package com.carmanagement.cli;


import com.carmanagement.cli.parser.CommandParser;
import com.carmanagement.cli.service.ApiClient;

/**
 * Main CLI application for Car Management System.
 * Provides commands to interact with the backend REST API.
 *
 * Supported commands:
 * - create-car --brand <brand> --model <model> --year <year>
 * - add-fuel --carId <id> --liters <liters> --price <price> --odometer <odometer>
 * - fuel-stats --carId <id>
 */
public class CarManagementCLI {

    private final ApiClient apiClient;

    public CarManagementCLI() {
        this.apiClient = new ApiClient();
    }

    /**
     * Main entry point for the CLI application.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        CarManagementCLI cli = new CarManagementCLI();

        if (args.length == 0) {
            cli.printUsage();
            return;
        }

        try {
            CommandParser parser = CommandParser.parse(args);
            cli.executeCommand(parser);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println();
            cli.printUsage();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Executes the parsed command.
     *
     * @param parser Parsed command
     * @throws Exception if command execution fails
     */
    private void executeCommand(CommandParser parser) throws Exception {
        String command = parser.getCommand();

        switch (command) {
            case "create-car":
                handleCreateCar(parser);
                break;
            case "add-fuel":
                handleAddFuel(parser);
                break;
            case "fuel-stats":
                handleFuelStats(parser);
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    /**
     * Handles the create-car command.
     * Creates a new car and displays the assigned ID.
     *
     * @param parser Command parser with arguments
     * @throws Exception if API call fails
     */
    private void handleCreateCar(CommandParser parser) throws Exception {
        String brand = parser.getRequiredArgument("brand");
        String model = parser.getRequiredArgument("model");
        int year = parser.getRequiredInt("year");

        System.out.println("Creating car...");
        String response = apiClient.createCar(brand, model, year);

        long carId = apiClient.extractCarId(response);

        System.out.println("✓ Car created successfully!");
        System.out.println("Car ID: " + carId);
        System.out.println("Brand: " + brand);
        System.out.println("Model: " + model);
        System.out.println("Year: " + year);
    }

    /**
     * Handles the add-fuel command.
     * Adds a fuel entry to an existing car.
     *
     * @param parser Command parser with arguments
     * @throws Exception if API call fails or car not found
     */
    private void handleAddFuel(CommandParser parser) throws Exception {
        long carId = parser.getRequiredLong("carId");
        double liters = parser.getRequiredDouble("liters");
        double price = parser.getRequiredDouble("price");
        int odometer = parser.getRequiredInt("odometer");

        System.out.println("Adding fuel entry for car ID " + carId + "...");

        try {
            apiClient.addFuelEntry(carId, liters, price, odometer);

            System.out.println("✓ Fuel entry added successfully!");
            System.out.println("Liters: " + liters + " L");
            System.out.println("Price: " + price);
            System.out.println("Odometer: " + odometer + " km");
        } catch (Exception e) {
            if (e.getMessage().contains("404")) {
                System.err.println("✗ Error: Car with ID " + carId + " not found.");
                System.err.println("Please create the car first using create-car command.");
            } else {
                throw e;
            }
        }
    }

    /**
     * Handles the fuel-stats command.
     * Retrieves and displays fuel statistics for a car.
     *
     * @param parser Command parser with arguments
     * @throws Exception if API call fails or car not found
     */
    private void handleFuelStats(CommandParser parser) throws Exception {
        long carId = parser.getRequiredLong("carId");

        System.out.println("Fetching fuel statistics for car ID " + carId + "...");
        System.out.println();

        try {
            String response = apiClient.getFuelStats(carId);
            String formattedStats = apiClient.formatFuelStats(response);

            System.out.println(formattedStats);
        } catch (Exception e) {
            if (e.getMessage().contains("404")) {
                System.err.println("✗ Error: Car with ID " + carId + " not found.");
                System.err.println("Please create the car first using create-car command.");
            } else {
                throw e;
            }
        }
    }

    /**
     * Prints usage information for the CLI.
     */
    private void printUsage() {
        System.out.println("Car Management CLI");
        System.out.println("==================");
        System.out.println();
        System.out.println("Usage: java -jar cli.jar <command> [options]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println();
        System.out.println("  create-car");
        System.out.println("    Create a new car");
        System.out.println("    Options:");
        System.out.println("      --brand <brand>    Car brand (required)");
        System.out.println("      --model <model>    Car model (required)");
        System.out.println("      --year <year>      Car year (required)");
        System.out.println("    Example:");
        System.out.println("      create-car --brand Toyota --model Corolla --year 2018");
        System.out.println();
        System.out.println("  add-fuel");
        System.out.println("    Add a fuel entry to a car");
        System.out.println("    Options:");
        System.out.println("      --carId <id>           Car ID (required)");
        System.out.println("      --liters <liters>      Liters of fuel (required)");
        System.out.println("      --price <price>        Total price (required)");
        System.out.println("      --odometer <reading>   Odometer reading (required)");
        System.out.println("    Example:");
        System.out.println("      add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000");
        System.out.println();
        System.out.println("  fuel-stats");
        System.out.println("    Get fuel statistics for a car");
        System.out.println("    Options:");
        System.out.println("      --carId <id>    Car ID (required)");
        System.out.println("    Example:");
        System.out.println("      fuel-stats --carId 1");
        System.out.println();
    }
}

