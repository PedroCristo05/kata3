package software.ulpgc.katas.model;

public record Game(
        String name,
        Platform platform,
        String genre,
        int year,
        double globalSales
) {
}