package com.mike.kniffel.game.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@NoArgsConstructor
public class GameRepository {
  HashMap<String, Game> games = new HashMap<>();

  public void save(final Game game) {
    this.games.put(game.getId(), game);
  }

  public List<Game> findAll() {
    return this.games.values().stream().toList();
  }

  public Optional<Game> findById(final String id) {
    return Optional.ofNullable(this.games.get(id));
  }

  public void deleteById(final String id) {
    this.games.remove(id);
  }
}
