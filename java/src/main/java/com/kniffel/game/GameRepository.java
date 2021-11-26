package com.kniffel.game;

import com.kniffel.player.Player;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class GameRepository {
    private static Integer nextGameId = 10000;
    private static HashMap<Integer, Game> games = new HashMap<>();

    public Game findById(Integer id) {
        return games.get(id);
    }

    public Game saveGame(Game game) {
        game.setId(nextGameId);
        games.put(game.getId(), game);

        nextGameId = (nextGameId + 1 > 99999) ? 10000 : nextGameId + 1;

        return game;
    }

    public Game findByPlayerSessionId(String sessionId) {
        for (Game game : games.values()) {
            for (Player player : game.getPlayers()) {
                if (player.getSessionId().equals(sessionId)) {
                    return game;
                }
            }
        }

        return null;
    }

    public boolean removeGameById(Integer id) {
        return games.remove(id) != null;
    }
}
