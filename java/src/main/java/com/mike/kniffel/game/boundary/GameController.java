package com.mike.kniffel.game.boundary;

import com.mike.kniffel.common.WebSocketRequest;
import com.mike.kniffel.common.WebSocketResponse;
import com.mike.kniffel.common.WebSocketResponseMessageType;
import com.mike.kniffel.common.WebSocketResponseStatus;
import com.mike.kniffel.game.boundary.dto.PlayerNameDTO;
import com.mike.kniffel.game.boundary.dto.TableCellDTO;
import com.mike.kniffel.game.control.GameService;
import com.mike.kniffel.game.entity.Game;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Controller
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@CrossOrigin("*")
@Validated
public class GameController {

  private final GameService gameService;
  private final SimpMessagingTemplate template;

  @PostMapping("/games")
  @ResponseBody
  public ResponseEntity<Game> createGame(@Valid @RequestBody final PlayerNameDTO playerNameDTO) {
    return new ResponseEntity<>(
        gameService.createGame(playerNameDTO.getPlayerName()), HttpStatus.CREATED);
  }

  @PostMapping("/games/{gameId}/join")
  @ResponseBody
  public ResponseEntity<Game> joinGame(
      @PathVariable final String gameId, @Valid @RequestBody final PlayerNameDTO playerNameDTO) {
    return ResponseEntity.ok(gameService.joinGame(gameId, playerNameDTO.getPlayerName()));
  }

  @GetMapping("/games")
  @ResponseBody
  public ResponseEntity<List<Game>> listGames() {
    return ResponseEntity.ok(gameService.listGames());
  }

  @GetMapping("/games/{gameId}")
  @ResponseBody
  public ResponseEntity<Game> getGame(@PathVariable final String gameId) {
    return ResponseEntity.ok(gameService.getGame(gameId));
  }

  //  <-- WebSocket Endpoints -->

  @MessageMapping("/games/{gameId}/connect")
  @SendTo("/topic/games/{gameId}")
  public WebSocketResponse connectToGame(
      @DestinationVariable final String gameId, final WebSocketRequest<Void> request) {

    return WebSocketResponse.builder()
        .game(gameService.connectToGame(gameId, request.getRequesterUUID()))
        .status(WebSocketResponseStatus.OK)
        .messageType(WebSocketResponseMessageType.PLAYER_CONNECTED)
        .build();
  }

  @MessageMapping("/games/{gameId}/start")
  @SendTo("/topic/games/{gameId}")
  public WebSocketResponse startGame(
      @DestinationVariable final String gameId, final @RequestBody WebSocketRequest<Void> request) {

    return WebSocketResponse.builder()
        .game(gameService.startGame(gameId, request.getRequesterUUID()))
        .status(WebSocketResponseStatus.OK)
        .messageType(WebSocketResponseMessageType.GAME_STARTED)
        .build();
  }

  @MessageMapping("/games/{gameId}/reset")
  @SendTo("/topic/games/{gameId}")
  public WebSocketResponse resetGame(
      @DestinationVariable final String gameId, final @RequestBody WebSocketRequest<Void> request) {

    return WebSocketResponse.builder()
        .game(gameService.resetGame(gameId, request.getRequesterUUID()))
        .status(WebSocketResponseStatus.OK)
        .messageType(WebSocketResponseMessageType.GAME_RESTARTED)
        .build();
  }

  @MessageMapping("/games/{gameId}/kick/{playerId}")
  @SendTo("/topic/games/{gameId}")
  public WebSocketResponse kickPlayerFromGame(
      @DestinationVariable final String gameId,
      @DestinationVariable final UUID playerId,
      final @RequestBody WebSocketRequest<Void> request) {

    return WebSocketResponse.builder()
        .game(gameService.kickPlayer(gameId, playerId, request.getRequesterUUID()))
        .status(WebSocketResponseStatus.OK)
        .messageType(WebSocketResponseMessageType.PLAYER_KICKED)
        .build();
  }

  @MessageMapping("/games/{gameId}/dices/roll")
  @SendTo("/topic/games/{gameId}")
  public WebSocketResponse rollDices(
      @DestinationVariable final String gameId, final @RequestBody WebSocketRequest<Void> request) {

    return WebSocketResponse.builder()
        .game(gameService.rollDices(gameId, request.getRequesterUUID()))
        .status(WebSocketResponseStatus.OK)
        .messageType(WebSocketResponseMessageType.DICE_ROLLED)
        .build();
  }

  @MessageMapping("/games/{gameId}/dices/{diceIndex}/click")
  @SendTo("/topic/games/{gameId}")
  public WebSocketResponse clickDice(
      @DestinationVariable final String gameId,
      @DestinationVariable final Integer diceIndex,
      final @RequestBody WebSocketRequest<Void> request) {

    return WebSocketResponse.builder()
        .game(gameService.clickDice(gameId, diceIndex, request.getRequesterUUID()))
        .status(WebSocketResponseStatus.OK)
        .messageType(WebSocketResponseMessageType.DICE_CLICKED)
        .build();
  }

  @MessageMapping("/games/{gameId}/table/click")
  @SendTo("/topic/games/{gameId}")
  public WebSocketResponse clickTableCell(
      @DestinationVariable final String gameId,
      final @RequestBody WebSocketRequest<TableCellDTO> request) {

    if (request.getPayload() == null) {
      return WebSocketResponse.builder()
          .status(WebSocketResponseStatus.INVALID_REQUEST)
          .messageType(WebSocketResponseMessageType.ERROR)
          .build();
    }
    return WebSocketResponse.builder()
        .game(
            gameService.clickTableCell(
                gameId,
                request.getPayload().getRow(),
                request.getPayload().getColumn(),
                request.getRequesterUUID()))
        .status(WebSocketResponseStatus.OK)
        .messageType(WebSocketResponseMessageType.TABLE_CELL_CLICKED)
        .build();
  }

  @EventListener
  public void onDisconnectEvent(final SessionDisconnectEvent event) {
    final UUID playerId =
        (UUID) SimpAttributesContextHolder.currentAttributes().getAttribute("playerId");
    final String gameId =
        (String) SimpAttributesContextHolder.currentAttributes().getAttribute("gameId");

    log.info(
        "DisconnectEvent for playerId: [{}] with gameId: [{}] and sessionId: [{}]",
        playerId,
        gameId,
        event.getSessionId());

    this.template.convertAndSend(
        "/topic/games/" + gameId,
        WebSocketResponse.builder()
            .status(WebSocketResponseStatus.OK)
            .messageType(WebSocketResponseMessageType.PLAYER_DISCONNECTED)
            .game(gameService.disconnectPlayer(gameId, playerId))
            .build());
  }
}
