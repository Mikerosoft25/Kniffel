package com.kniffel.game;

import com.kniffel.dice.Dice;
import com.kniffel.general.Request;
import com.kniffel.general.Response;
import com.kniffel.general.StatusMessage;
import com.kniffel.player.Player;
import com.kniffel.tableCell.TableCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    GameRepository gameRepository;

    public Response<Game> createGame(Request request) {

        Player host = request.getPlayer();

        host.setId(0);
        host.setHost(true);
        host.setSessionId(SimpAttributesContextHolder.currentAttributes().getSessionId());

        Game game = new Game();
        game.addPlayer(host);

        Game savedGame = this.gameRepository.saveGame(game);

        host.setGameId(savedGame.getId());

        Response<Game> response = new Response<>();
        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);

        return response;
    }

    public Response<Game> joinGame(Request request) {
        Response<Game> response = new Response<Game>();

        Player newPlayer = request.getPlayer();

        int gameId = newPlayer.getGameId();

        Game game = this.gameRepository.findById(gameId);

        if (game == null) {
            response.setStatusMessage(StatusMessage.GAME_NOT_FOUND);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        } else if (game.isStarted()) {
            response.setStatusMessage(StatusMessage.GAME_ALREADY_STARTED);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        }

        newPlayer.setId(game.getPlayerCount());
        newPlayer.setHost(false);
        newPlayer.setSessionId(SimpAttributesContextHolder.currentAttributes().getSessionId());

        game.addPlayer(newPlayer);

        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);

        return response;
    }

    public Response<Game> findGame(Integer gameId) {
        Response<Game> response = new Response<Game>();

        Game game = this.gameRepository.findById(gameId);

        if (game == null) {
            response.setStatusMessage(StatusMessage.GAME_NOT_FOUND);
            return response;
        }

        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);

        return response;
    }

    public Response<Game> startGame(Request request) {
        Response<Game> response = new Response<Game>();

        Player player = request.getPlayer();

        if (!player.isHost()) {
            response.setStatusMessage(StatusMessage.NOT_HOST_TRIES_START);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        }

        Game game = this.gameRepository.findById(player.getGameId());

        game.setStarted(true);
        game.setEnded(false);
        game.setRestarted(false);

        // setting the starting player to the first still connected player
        for (Player gamePlayer : game.getPlayers()) {
            if (gamePlayer.isConnected()) {
                game.setPlayerTurn(gamePlayer.getId());
                break;
            }
        }

        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);

        return response;
    }

    public Response<Game> clickDice(Request request) {
        Response<Game> response = new Response<Game>();

        Player player = request.getPlayer();
        Dice dice = request.getDice();

        Game game = this.gameRepository.findById(player.getGameId());

        if (player.getId() != game.getPlayerTurn()) {
            response.setStatusMessage(StatusMessage.WRONG_PLAYER_DICE_CLICK);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        } else if (game.getRollCount() == 3) {
            response.setStatusMessage(StatusMessage.DICE_NOT_ROLLED);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        } else if (game.getRoundCount() == 14) {
            response.setStatusMessage(StatusMessage.GAME_ENDED);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        }

        game.changeDiceLock(dice.getIndex());

        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);
        return response;
    }

    public Response<Game> rollDices(Request request) {
        Response<Game> response = new Response<Game>();

        Player player = request.getPlayer();

        Game game = this.gameRepository.findById(player.getGameId());

        if (player.getId() != game.getPlayerTurn()) {
            response.setStatusMessage(StatusMessage.WRONG_PLAYER_DICE_ROLL);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        } else if (game.getRollCount() == 0) {
            response.setStatusMessage(StatusMessage.NO_MORE_ROLLS);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        }

        game.rollDices();

        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);
        return response;
    }

    public Response<Game> clickCell(Request request) {
        Response<Game> response = new Response<Game>();

        Player player = request.getPlayer();
        TableCell tableCell = request.getTableCell();

        Game game = this.gameRepository.findById(player.getGameId());

        if (player.getId() != game.getPlayerTurn()) {
            response.setStatusMessage(StatusMessage.WRONG_PLAYER_CELL_CLICK);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        } else if (player.getId() != tableCell.getCol()) {
            response.setStatusMessage(StatusMessage.WRONG_COLUMN_CLICK);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        } else if (!game.cellIsFree(player.getId(), tableCell.getRow())) {
            response.setStatusMessage(StatusMessage.CELL_NOT_EMPTY);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        } else if (game.getRollCount() == 3) {
            response.setStatusMessage(StatusMessage.PLAYER_NEEDS_TO_ROLL);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        }

        game.determinePoints(tableCell.getRow());
        game.setNextPlayerTurn();

        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);
        return response;
    }

    public Response<Game> restartGame(Request request) {
        Response<Game> response = new Response<Game>();

        Player player = request.getPlayer();

        if (!player.isHost()) {
            response.setStatusMessage(StatusMessage.NOT_HOST_TRIES_RESTART);
            response.setTargetUUID(request.getRequesterUUID());
            return response;
        }

        Game game = this.gameRepository.findById(player.getGameId());

        game.reset();
        game.setStarted(false);

        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);
        return response;
    }

    public void disconnectPlayer(String sessionId) {
        Game game = this.gameRepository.findByPlayerSessionId(sessionId);

        if (game == null) {
            return;
        }

        for (Player player : game.getPlayers()) {
            if (player.getSessionId().equals(sessionId)) {
                player.setConnected(false);
            }

            Player nextActivePlayer = null;

            if (player.isHost()) {
                player.setHost(false);


                for (Player activePlayer : game.getPlayers()) {
                    if (activePlayer.isConnected()) {
                        nextActivePlayer = activePlayer;
                        nextActivePlayer.setHost(true);
                        break;
                    }
                }

                if (nextActivePlayer == null) {
                    this.gameRepository.removeGameById(game.getId());
                    return;
                }
            }

            if (player.getId() == game.getPlayerTurn()) {
                game.setPlayerTurn(nextActivePlayer.getId());
            }
        }

        Response<Game> response = new Response<Game>();
        response.setContent(game);
        response.setStatusMessage(StatusMessage.OK);

        this.simpMessagingTemplate.convertAndSend("/topic/game/"  + game.getId(), response);
    }
}
