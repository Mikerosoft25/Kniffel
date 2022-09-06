package com.mike.kniffel.game.control;

import com.mike.kniffel.game.entity.GameRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GameIdGenerator {

  private GameRepository gameRepository;

  public String generate() {
    String generatedId;
    do {
      generatedId = UUID.randomUUID().toString().substring(0, 5);
    } while (gameRepository.findById(generatedId).isPresent());

    return generatedId;
  }
}
