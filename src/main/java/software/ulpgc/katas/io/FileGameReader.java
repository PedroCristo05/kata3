package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileGameReader implements GameReader {

    private final File file;
    private final CsvGameParser parser;

    public FileGameReader(File file) {
        this(file, new CsvGameParser());
    }

    public FileGameReader(File file, CsvGameParser parser) {
        this.file = file;
        this.parser = parser;
    }

    @Override
    public LoadResult load() {
        try (InputStream is = new FileInputStream(file)) {
            return loadFrom(is);
        } catch (FileNotFoundException e) {
            System.out.println("Aviso: El archivo no existe o la ruta es inválida -> " + file.getPath());
            return new LoadResult(List.of(), 0);
        } catch (IOException e) {
            System.out.println("Error de lectura al procesar el archivo -> " + e.getMessage());
            return new LoadResult(List.of(), 0);
        }
    }

    public LoadResult loadFrom(InputStream is) {
        List<Game> games = new ArrayList<>();
        int discarded = 0;

        List<String> lines = new BufferedReader(new InputStreamReader(is))
                .lines().skip(1).toList();

        for (String line : lines) {
            Game game = parser.parse(line);
            if (game != null) games.add(game);
            else discarded++;
        }

        return new LoadResult(games, discarded);
    }
}
