package com.carmanagement.cli.parser;


import java.util.HashMap;
import java.util.Map;

/**
 * Parses command-line arguments into a structured format.
 * Handles arguments in the format: command --flag value
 */
public class CommandParser {

    private final String command;
    private final Map<String, String> arguments;

    private CommandParser(String command, Map<String, String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    /**
     * Parses command-line arguments.
     * Expected format: command --arg1 value1 --arg2 value2
     *
     * @param args Command-line arguments
     * @return CommandParser instance
     * @throws IllegalArgumentException if no command provided
     */
    public static CommandParser parse(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No command provided");
        }

        String command = args[0];
        Map<String, String> arguments = new HashMap<>();

        // Parse arguments in --key value format
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String key = args[i].substring(2); // Remove "--" prefix

                if (i + 1 < args.length && !args[i + 1].startsWith("--")) {
                    String value = args[i + 1];
                    arguments.put(key, value);
                    i++; // Skip next item since it's the value
                } else {
                    throw new IllegalArgumentException(
                            "Missing value for argument: " + args[i]);
                }
            }
        }

        return new CommandParser(command, arguments);
    }

    public String getCommand() {
        return command;
    }

    /**
     * Gets a required argument value.
     *
     * @param key Argument key
     * @return Argument value
     * @throws IllegalArgumentException if argument is missing
     */
    public String getRequiredArgument(String key) {
        if (!arguments.containsKey(key)) {
            throw new IllegalArgumentException("Missing required argument: --" + key);
        }
        return arguments.get(key);
    }

    /**
     * Gets an argument as an integer.
     *
     * @param key Argument key
     * @return Integer value
     * @throws IllegalArgumentException if argument is missing or not a valid integer
     */
    public int getRequiredInt(String key) {
        String value = getRequiredArgument(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Argument --" + key + " must be a valid integer");
        }
    }

    /**
     * Gets an argument as a long.
     *
     * @param key Argument key
     * @return Long value
     * @throws IllegalArgumentException if argument is missing or not a valid long
     */
    public long getRequiredLong(String key) {
        String value = getRequiredArgument(key);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Argument --" + key + " must be a valid number");
        }
    }

    /**
     * Gets an argument as a double.
     *
     * @param key Argument key
     * @return Double value
     * @throws IllegalArgumentException if argument is missing or not a valid double
     */
    public double getRequiredDouble(String key) {
        String value = getRequiredArgument(key);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Argument --" + key + " must be a valid decimal number");
        }
    }
}

