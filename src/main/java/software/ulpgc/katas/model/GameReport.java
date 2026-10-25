package software.ulpgc.katas.model;

import java.time.Instant;
import java.util.List;

public record GameReport(
        Metadata metadata,
        List<Game> games
) {
    public record Metadata(
            Instant generatedAt,
            int totalRecords,
            int discardedRecords,
            double totalGlobalSales
    ) {
    }
}