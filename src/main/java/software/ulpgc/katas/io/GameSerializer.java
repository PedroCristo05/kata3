package software.ulpgc.katas.io;

import software.ulpgc.katas.model.GameReport;

import java.io.OutputStream;

public interface GameSerializer {
    void serialize(GameReport report, OutputStream outputStream);
}
