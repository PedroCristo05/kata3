package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Game;
import software.ulpgc.katas.model.Platform;

public class CsvGameParser {

    public Game parse(String line) {
        return parse(line.split(","));
    }

    public Game parse(String[] fields) {
        if (fields.length < 11) return null;

        try {
            return new Game(
                    fields[1].trim(),
                    platformOf(fields[2].trim()),
                    fields[4].trim(),
                    parseIntOrDefault(fields[3].trim(), 0),
                    parseDoubleOrDefault(fields[10].trim(), 0.0)
            );
        } catch (Exception e) {
            return null;
        }
    }

    private Platform platformOf(String field) {
        try {
            return Platform.valueOf(field);
        } catch (IllegalArgumentException e) {
            return Platform.Other;
        }
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}