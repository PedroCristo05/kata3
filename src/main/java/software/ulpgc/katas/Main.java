package software.ulpgc.katas;

import software.ulpgc.katas.io.*;
import software.ulpgc.katas.model.Game;
import software.ulpgc.katas.model.GameReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File file = new File("vgsales.csv");
        GameReader reader = new FileGameReader(file);
        GameSerializer serializer = new GsonGameSerializer();

        // 1. Carga de datos y cálculo de métricas agregadas
        LoadResult result = reader.load();
        List<Game> games = result.games();

        double totalSales = games.stream()
                .mapToDouble(Game::globalSales)
                .sum();

        GameReport.Metadata metadata = new GameReport.Metadata(
                Instant.now(),
                games.size(),
                result.discardedCount(),
                Math.round(totalSales * 100.0) / 100.0
        );

        GameReport report = new GameReport(metadata, games);

        // 2. Serialización a Consola (System.out)
        System.out.println("=== 1. JSON Generado (Salida estándar) ===");
        serializer.serialize(report, System.out);

        // 3. Serialización a Fichero en disco (output.json)
        File outputFile = new File("output.json");
        try (OutputStream fos = new FileOutputStream(outputFile)) {
            serializer.serialize(report, fos);
            System.out.println("\nReporte JSON guardado con éxito en: " + outputFile.getName());
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo JSON: " + e.getMessage());
        }

        // 4. Verificación de caso de error controlado (fichero inexistente)
        System.out.println("\n=== 2. Comprobación defensiva con fichero inexistente ===");
        GameReader brokenReader = new FileGameReader(new File("archivo_inexistente.csv"));
        LoadResult emptyResult = brokenReader.load();
        System.out.println("Registros leídos: " + emptyResult.games().size() +
                " | Descartes: " + emptyResult.discardedCount());
    }
}