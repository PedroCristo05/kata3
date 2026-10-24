package software.ulpgc.katas.io;

import software.ulpgc.katas.model.Game;

import java.util.List;

public record LoadResult(
        List<Game> games,
        int discardedCount
) {
}
