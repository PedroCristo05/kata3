package software.ulpgc.katas.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import software.ulpgc.katas.model.GameReport;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class GsonGameSerializer implements GameSerializer{

    private final Gson gson;

    public GsonGameSerializer() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.toString()))
                .create();
    }

    @Override
    public void serialize(GameReport report, OutputStream outputStream) {
        PrintWriter wirter = new PrintWriter(outputStream, true, StandardCharsets.UTF_8);
        wirter.println(gson.toJson(report));
    }
}
