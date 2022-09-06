package com.mike.kniffel.game.control;

import com.mike.kniffel.common.WebSocketResponse;
import com.mike.kniffel.common.WebSocketResponseMessageType;
import com.mike.kniffel.common.WebSocketResponseStatus;
import com.mike.kniffel.exception.rest.ErrorType;
import com.mike.kniffel.exception.rest.KniffelException;
import com.mike.kniffel.exception.ws.IllegalWebSocketRequestException;
import com.mike.kniffel.game.entity.Dice;
import com.mike.kniffel.game.entity.Game;
import com.mike.kniffel.game.entity.GameRepository;
import com.mike.kniffel.game.entity.GameStatus;
import com.mike.kniffel.game.entity.Player;
import com.mike.kniffel.game.entity.PointsCalculateMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class GameService {

  private static final int GAME_MAX_PLAYERS = 8;

  private final GameRepository gameRepository;
  private final GameIdGenerator gameIdGenerator;
  private final SimpMessagingTemplate messagingTemplate;

  public Game createGame(final String playerName) {
    final Player host =
        Player.builder().id(UUID.randomUUID()).name(playerName).host(true).connected(false).build();

    final Game game =
        Game.builder()
            .id(gameIdGenerator.generate())
            .players(new ArrayList<>(List.of(host)))
            .status(GameStatus.CREATED)
            .build();

    gameRepository.save(game);
    log.info(
        "Player: [{}, {}] created a new game with gameId: [{}]",
        host.getName(),
        host.getId(),
        game.getId());
    return game;
  }

  public Game joinGame(final String gameId, final String playerName) {
    final Game game =
        gameRepository
            .findById(gameId)
            .orElseThrow(() -> new KniffelException(ErrorType.GAME_NOT_FOUND));

    if (game.getPlayers().size() == GAME_MAX_PLAYERS) {
      log.info(
          "Player: [{}] could not join game with gameId: [{}] because the game is already full",
          playerName,
          gameId);
      throw new KniffelException(ErrorType.GAME_IS_FULL);
    }

    if (game.getStatus() != GameStatus.CREATED) {
      log.info(
          "Player: [{}] could not join game with gameId: [{}] because the game already started",
          playerName,
          gameId);
      throw new KniffelException(ErrorType.Game_ALREADY_STARTED);
    }

    for (final Player connectedPlayer : game.getPlayers()) {
      if (connectedPlayer.getName().equalsIgnoreCase(playerName)) {
        log.info(
            "Player: [{}] could not join game with gameId: [{}] because a player with the same name already exists",
            playerName,
            gameId);
        throw new KniffelException(ErrorType.PLAYER_NAME_ALREADY_EXISTS);
      }
    }

    final Player player =
        Player.builder()
            .id(UUID.randomUUID())
            .name(playerName)
            .host(false)
            .connected(false)
            .build();

    game.addPlayer(player);
    log.info(
        "Player: [{}, {}] joined game with gameId: [{}]",
        player.getName(),
        player.getId(),
        game.getId());
    return game;
  }

  public List<Game> listGames() {
    return gameRepository.findAll();
  }

  public Game getGame(final String gameId) {
    return gameRepository
        .findById(gameId)
        .orElseThrow(() -> new KniffelException(ErrorType.GAME_NOT_FOUND));
  }

  public Game connectToGame(final String gameId, final UUID requesterUUID) {
    SimpAttributesContextHolder.currentAttributes().setAttribute("gameId", gameId);
    SimpAttributesContextHolder.currentAttributes().setAttribute("playerId", requesterUUID);

    final Game game = findGameById(gameId);
    final Player player = findPlayerById(game, requesterUUID);
    player.setConnected(true);

    log.info(
        "Player: [{}, {}] connected to game with gameId: [{}]",
        player.getName(),
        player.getId(),
        game.getId());
    return game;
  }

  public Game disconnectPlayer(final String gameId, final UUID playerId) {
    final Game game = findGameById(gameId);
    final Player disconnectedPlayer = findPlayerById(game, playerId);

    game.getPlayers().removeIf((player -> player.getId().equals(playerId)));
    log.info("Player with id: [{}] was removed from game: [{}]", playerId, game.getId());

    if (game.getPlayers().size() == 0) {
      gameRepository.deleteById(gameId);
      log.info("Game: [{}] was deleted because all players disconnected", gameId);
      game.setStatus(GameStatus.ENDED);
      return game;
    }

    if (disconnectedPlayer.isHost()) {
      final Player newHost = game.getPlayers().get(0);
      newHost.setHost(true);

      sendHostChangeMessage(game, newHost.getId());
      log.info("Host has left the game. New host: [{}, {}]", newHost.getName(), newHost.getId());
    }
    gameRepository.save(game);
    return game;
  }

  public Game kickPlayer(final String gameId, final UUID playerId, final UUID requesterUUID) {
    final Game game = findGameById(gameId);

    final Player requester = findPlayerById(game, requesterUUID);
    if (!requester.isHost()) {
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_IS_NOT_HOST);
    }

    game.getPlayers().removeIf((player -> player.getId().equals(playerId)));
    log.info("Player with id: [{}] was kicked from game: [{}]", playerId, game.getId());

    gameRepository.save(game);
    return game;
  }

  public Game startGame(final String gameId, final UUID requesterUUID) {
    final Game game = findGameById(gameId);
    final Player requester = findPlayerById(game, requesterUUID);

    if (!requester.isHost()) {
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_IS_NOT_HOST);
    }

    final Dice[] dices = new Dice[5];
    for (int i = 0; i < 5; i++) {
      dices[i] = new Dice(1, false, i);
    }

    game.setDices(dices);
    game.setPlayerTurn(0);
    game.setRollCount(3);
    game.setRound(1);
    game.setStatus(GameStatus.STARTED);
    log.info("Player: [{}, {}] started game: [{}]", requester.getName(), requester.getId(), gameId);
    return game;
  }

  public Game resetGame(final String gameId, final UUID requesterUUID) {
    final Game game = findGameById(gameId);
    final Player requester = findPlayerById(game, requesterUUID);

    if (!requester.isHost()) {
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_IS_NOT_HOST);
    }

    game.getPlayers().forEach(Player::resetPoints);
    game.setStatus(GameStatus.CREATED);
    game.setDices(null);
    game.setRollCount(null);
    game.setPlayerTurn(null);
    game.setRound(null);

    gameRepository.save(game);
    return game;
  }

  public Game rollDices(final String gameId, final UUID requesterUUID) {
    final Game game = findGameById(gameId);

    log.info("Player with id: [{}] rolled the dices", requesterUUID);
    if (playerIsNotAtTurn(game, requesterUUID)) {
      log.info("Dice were not rolled because player with id: [{}] ist not at turn", requesterUUID);
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_IS_NOT_AT_TURN);
    }

    if (game.getRollCount() == 0) {
      log.info("Dice were not rolled because player has no more rolls");
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.NO_MORE_ROLL);
    }

    for (final Dice dice : game.getDices()) {
      if (!dice.isLocked()) {
        dice.setValue(ThreadLocalRandom.current().nextInt(1, 7));
      }
    }
    game.setRollCount(game.getRollCount() - 1);
    gameRepository.save(game);
    return game;
  }

  public Game clickDice(final String gameId, final int diceIndex, final UUID requesterUUID) {
    final Game game = findGameById(gameId);

    log.info("Player with id: [{}] clicked dice: [{}]", requesterUUID, diceIndex);
    if (game.getRollCount() == 3) {
      log.info("Points could not be entered because player [{}] has not rolled yet", requesterUUID);
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_HAS_NOT_ROLLED);
    }

    if (playerIsNotAtTurn(game, requesterUUID)) {
      log.info(
          "Dice lock was not changed because player with id: [{}] is not at turn", requesterUUID);
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_IS_NOT_AT_TURN);
    }

    game.changeDiceLock(diceIndex);
    gameRepository.save(game);
    return game;
  }

  public Game clickTableCell(
      final String gameId, final int row, final int column, final UUID requesterUUID) {
    final Game game = findGameById(gameId);
    final Player player = findPlayerById(game, requesterUUID);
    log.info(
        "Player: [{}] clicked table cell, row: [{}], col: [{}]", player.getName(), row, column);

    if (game.getRollCount() == 3) {
      log.info(
          "Points could not be entered because player [{}] has not rolled yet", player.getName());
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_HAS_NOT_ROLLED);
    }

    if (playerIsNotAtTurn(game, requesterUUID)) {
      log.info("Points could not be entered because player [{}] is not at turn", player.getName());
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_IS_NOT_AT_TURN);
    }

    final Player playerOfColumn = game.getPlayers().get(column);
    if (!playerOfColumn.getId().equals(requesterUUID)) {
      log.info(
          "Points could not be entered because player with id: [{}] tried to enter points in wrong column: [{}] of player: [{}]",
          requesterUUID,
          column,
          playerOfColumn.getId());
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.WRONG_TABLE_COLUMN);
    }

    updatePlayerPoints(player, game.getDices(), row);

    game.unlockAllDice();
    game.setRollCount(3);
    if (game.getPlayerTurn() == game.getPlayers().size() - 1) {
      if (game.getRound() == 2) {
        game.setStatus(GameStatus.ENDED);
      } else {
        game.setRound(game.getRound() + 1);
        game.setPlayerTurn(0);
      }
    } else {
      game.setPlayerTurn(game.getPlayerTurn() + 1);
    }

    gameRepository.save(game);
    return game;
  }

  private Game findGameById(final String gameId) {
    return gameRepository
        .findById(gameId)
        .orElseThrow(
            () -> {
              log.info("Game with id: [{}] could not be found", gameId);
              throw new IllegalWebSocketRequestException(WebSocketResponseStatus.GAME_NOT_FOUND);
            });
  }

  private Player findPlayerById(final Game game, final UUID playerId) {
    return game.getPlayers().stream()
        .filter(player -> player.getId().equals(playerId))
        .findFirst()
        .orElseThrow(
            () -> {
              log.info("Player with id: [{}] could not be found", playerId);
              throw new IllegalWebSocketRequestException(WebSocketResponseStatus.PLAYER_NOT_FOUND);
            });
  }

  private boolean playerIsNotAtTurn(final Game game, final UUID playerId) {
    return !game.getPlayers().get(game.getPlayerTurn()).getId().equals(playerId);
  }

  private void sendHostChangeMessage(final Game game, final UUID playerId) {
    this.messagingTemplate.convertAndSend(
        "/topic/games/" + game.getId() + "/player/" + playerId,
        WebSocketResponse.builder()
            .status(WebSocketResponseStatus.OK)
            .messageType(WebSocketResponseMessageType.PLAYER_IS_NOW_HOST)
            .game(game)
            .build());
  }

  private void updatePlayerPoints(final Player player, final Dice[] dices, final int row) {
    final Integer[] playerPoints = player.getPoints();
    if (playerPoints[row] != -1) {
      throw new IllegalWebSocketRequestException(WebSocketResponseStatus.TABLE_CELL_NOT_EMPTY);
    }

    playerPoints[row] =
        GamePointsCalculator.calculatePoints(dices, PointsCalculateMode.mapRowToCalculateMode(row));

    int upperSectionSum = 0;
    for (int i = 0; i < 6; i++) {
      upperSectionSum += playerPoints[i] != -1 ? playerPoints[i] : 0;
    }

    final int bonus = upperSectionSum >= 63 ? 35 : 0;
    final int upperSectionSumWithBonus = upperSectionSum + bonus;

    int lowerSectionSum = 0;
    for (int i = 9; i < 16; i++) {
      lowerSectionSum += playerPoints[i] != -1 ? playerPoints[i] : 0;
    }

    final int totalPoints = lowerSectionSum + upperSectionSum;

    playerPoints[6] = upperSectionSum;
    playerPoints[7] = bonus;
    playerPoints[8] = upperSectionSumWithBonus;
    playerPoints[16] = lowerSectionSum;
    playerPoints[17] = upperSectionSumWithBonus;
    playerPoints[18] = totalPoints;

    player.setPoints(playerPoints);
  }
}
