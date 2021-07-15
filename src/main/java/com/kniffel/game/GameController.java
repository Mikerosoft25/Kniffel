package com.kniffel.game;

import com.kniffel.general.Request;
import com.kniffel.general.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;

    @MessageMapping("/game/create")
    @SendTo("/topic/game/create")
    public Response<Game> createGame(Request request) {
        return this.gameService.createGame(request);
    }

    @MessageMapping("/game/join/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Response<Game> joinGame(Request request) {
        return this.gameService.joinGame(request);
    }

    @MessageMapping("/game/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Response<Game> findGame(@DestinationVariable Integer gameId) {
        return this.gameService.findGame(gameId);
    }

    @MessageMapping("/game/start/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Response<Game> startGame(Request request) {
        return this.gameService.startGame(request);
    }

    @MessageMapping("/game/dice/click/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Response<Game> clickDice(Request request) {
        return this.gameService.clickDice(request);
    }

    @MessageMapping("/game/dice/roll/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Response<Game> rollDices(Request request) {
        return this.gameService.rollDices(request);
    }

    @MessageMapping("/game/cell/click/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Response<Game> clickCell(Request request) {
        return this.gameService.clickCell(request);
    }

    @MessageMapping("/game/restart/{gameId}")
    @SendTo("/topic/game/{gameId}")
    public Response<Game> restartGame(Request request) {
        return this.gameService.restartGame(request);
    }
}
