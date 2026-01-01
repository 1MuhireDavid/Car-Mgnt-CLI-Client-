package com.carmanagement.cli.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * HTTP client service for communicating with the backend API.
 * Uses java.net.http.HttpClient for HTTP communication.
 */
public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient httpClient;
    private final Gson gson;

    public ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
    }

    /**
     * Creates a new car via POST /api/cars.
     *
     * @param brand Car brand
     * @param model Car model
     * @param year Car year
     * @return JSON response as string
     * @throws Exception if request fails
     */
    public String createCar(String brand, String model, int year) throws Exception {
        JsonObject carJson = new JsonObject();
        carJson.addProperty("brand", brand);
        carJson.addProperty("model", model);
        carJson.addProperty("year", year);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(carJson.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 201) {
            return response.body();
        } else {
            throw new Exception("Failed to create car. Status: " + response.statusCode());
        }
    }

    /**
     * Adds a fuel entry via POST /api/cars/{id}/fuel.
     *
     * @param carId Car ID
     * @param liters Liters of fuel
     * @param price Price paid
     * @param odometer Odometer reading
     * @return JSON response as string
     * @throws Exception if request fails or car not found
     */
    public String addFuelEntry(long carId, double liters, double price, int odometer)
            throws Exception {
        JsonObject fuelJson = new JsonObject();
        fuelJson.addProperty("liters", liters);
        fuelJson.addProperty("price", price);
        fuelJson.addProperty("odometer", odometer);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(fuelJson.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else if (response.statusCode() == 404) {
            throw new Exception("Car with ID " + carId + " not found (404)");
        } else {
            throw new Exception("Failed to add fuel entry. Status: " + response.statusCode());
        }
    }

    /**
     * Retrieves fuel statistics via GET /api/cars/{id}/fuel/stats.
     *
     * @param carId Car ID
     * @return JSON response as string
     * @throws Exception if request fails or car not found
     */
    public String getFuelStats(long carId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/cars/" + carId + "/fuel/stats"))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else if (response.statusCode() == 404) {
            throw new Exception("Car with ID " + carId + " not found (404)");
        } else {
            throw new Exception("Failed to get fuel stats. Status: " + response.statusCode());
        }
    }

    /**
     * Parses a JSON response and extracts the car ID.
     *
     * @param jsonResponse JSON response string
     * @return Car ID
     */
    public long extractCarId(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        return jsonObject.get("id").getAsLong();
    }

    /**
     * Formats fuel statistics for display.
     *
     * @param jsonResponse JSON stats response
     * @return Formatted string
     */
    public String formatFuelStats(String jsonResponse) {
        JsonObject stats = JsonParser.parseString(jsonResponse).getAsJsonObject();

        double totalFuel = stats.get("totalFuel").getAsDouble();
        double totalCost = stats.get("totalCost").getAsDouble();
        double avgConsumption = stats.get("averageConsumptionPer100Km").getAsDouble();

        return String.format(
                "Total fuel: %.1f L%n" +
                        "Total cost: %.2f%n" +
                        "Average consumption: %.1f L/100km",
                totalFuel, totalCost, avgConsumption
        );
    }
}

