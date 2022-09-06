import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CompatClient, Stomp } from '@stomp/stompjs';
import { Observable, Subject } from 'rxjs';
import { environment } from 'src/environments/environment';
import {
  Game,
  GameStatus,
  Player,
  TableCell,
  WebSocketRequest,
  WebSocketResponse,
  WebSocketResponseMessageType,
  WebSocketResponseStatus,
} from '../models/model';
import { SnackbarService } from '../snackbar/snackbar.service';

const httpHeaders: HttpHeaders = new HttpHeaders({
  'Content-Type': 'application/json',
  Accept: 'application/json',
  'Cache-Control': 'no-cache',
});

const httpOptions = {
  headers: httpHeaders,
};

@Injectable({
  providedIn: 'root',
})
export class GameService {
  private REST_API_URL: string = environment.REST_API_URL;
  private WEBSOCKET_URL: string = environment.WEBSOCKET_URL;

  private stompClient: CompatClient | undefined;

  latestGameState: Game | undefined;
  game: Subject<Game> = new Subject<Game>();
  player: Player | undefined;

  constructor(
    private http: HttpClient,
    private router: Router,
    private snackbarService: SnackbarService
  ) {
    let player1: Player = {
      id: '58a368b7-19be-4489-bd45-46c58eef9248',
      name: 'Player1',
      host: true,
      connected: false,
      points: [
        -1, -1, -1, -1, -1, -1, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0,
      ],
    };

    let player2: Player = {
      id: 'abcdefg-19be-4489-bd45-46c58eef9248',
      name: 'Player2',
      host: true,
      connected: false,
      points: [
        -1, -1, -1, -1, -1, -1, 0, 0, 0, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0,
      ],
    };

    let game = {
      id: 'fca9d',
      players: [player1, player2],
      dices: [
        {
          value: 1,
          locked: false,
          index: 0,
        },
        {
          value: 1,
          locked: true,
          index: 1,
        },
        {
          value: 1,
          locked: true,
          index: 2,
        },
        {
          value: 1,
          locked: false,
          index: 3,
        },
        {
          value: 1,
          locked: true,
          index: 4,
        },
      ],
      // status: GameStatus.STARTED,
      status: GameStatus.ENDED,
      playerTurn: 0,
      rollCount: 0,
      round: 1,
    };

    this.player = player1;
    this.latestGameState = game;
    this.game.next(game);
  }

  reset() {
    this.stompClient?.disconnect();
    this.stompClient = undefined;
    this.game = new Subject<Game>();
    this.player = undefined;
  }

  isConnected(): boolean {
    console.log(this.stompClient?.connected);
    return this.stompClient !== undefined;
  }

  connectToGame(gameId: string) {
    this.stompClient = Stomp.client(this.WEBSOCKET_URL);
    this.stompClient.connect(
      {},
      () => {
        this.stompClient?.subscribe(
          `/topic/games/${gameId}`,
          (response: any) => {
            this.handleWebSocketResponse(JSON.parse(response.body));
          }
        );

        this.stompClient?.subscribe(
          `/topic/games/${gameId}/player/${this.player?.id}`,
          (response: any) => {
            this.handleWebSocketResponse(JSON.parse(response.body));
          }
        );

        this.stompClient?.send(
          `/app/games/${gameId}/connect`,
          {},
          JSON.stringify({ requesterUUID: this.player?.id })
        );
      },
      (error: Error) => {
        console.log(error);
        this.snackbarService.showMessage(
          'Connection to the server has been lost.'
        );
        this.router.navigateByUrl('/');
      },
      (closeEvent: CloseEvent) => {
        console.log(closeEvent);
        if (closeEvent.code === 1001) {
          this.snackbarService.showMessage(
            'Connection to the server has been lost.'
          );
        }
        this.router.navigateByUrl('/');
      }
    );
  }

  createGame(playerName: string): Observable<Game> {
    return this.http.post<Game>(`${this.REST_API_URL}/games`, {
      playerName: playerName,
    });
  }

  joinGame(playerName: string, gameId: string): Observable<Game> {
    return this.http.post<Game>(`${this.REST_API_URL}/games/${gameId}/join`, {
      playerName: playerName,
    });
  }

  listGames(): Observable<Game[]> {
    return this.http.get<Game[]>(`${this.REST_API_URL}/games`);
  }

  getGame(id: string): Observable<Game> {
    return this.http.get<Game>(`${this.REST_API_URL}/games/${id}`, httpOptions);
  }

  kickPlayer(kickedPlayerId: string, gameId: string): void {
    this.stompClient?.send(
      `/app/games/${gameId}/kick/${kickedPlayerId}`,
      {},
      JSON.stringify({ requesterUUID: this.player?.id })
    );
  }

  startGame(gameId: string): void {
    this.stompClient?.send(
      `/app/games/${gameId}/start`,
      {},
      JSON.stringify({ requesterUUID: this.player?.id })
    );
  }

  resetGame(gameId: string): void {
    this.stompClient?.send(
      `/app/games/${gameId}/reset`,
      {},
      JSON.stringify({ requesterUUID: this.player?.id })
    );
  }

  rollDice(gameId: string): void {
    this.stompClient?.send(
      `/app/games/${gameId}/dices/roll`,
      {},
      JSON.stringify({ requesterUUID: this.player?.id })
    );
  }

  changeDiceLock(gameId: string, diceIndex: number): void {
    this.stompClient?.send(
      `/app/games/${gameId}/dices/${diceIndex}/click`,
      {},
      JSON.stringify({ requesterUUID: this.player?.id })
    );
  }

  clickTableCell(gameId: string, tableCell: TableCell): void {
    if (!this.player || !this.player.id) {
      return;
    }

    let request: WebSocketRequest<TableCell> = {
      requesterUUID: this.player?.id,
      payload: tableCell,
    };

    this.stompClient?.send(
      `/app/games/${gameId}/table/click`,
      {},
      JSON.stringify(request)
    );
  }

  determineScoreboard(game: Game): String[] {
    let players = [...game.players];
    players.sort((a, b) => a.points[18] - b.points[18]);

    let scoreboard: String[] = [];
    players.forEach((player) => {
      scoreboard.push(player.name);
    });

    return scoreboard;
  }

  private handleWebSocketResponse(response: WebSocketResponse): void {
    console.log(response);
    if (response.status !== WebSocketResponseStatus.OK) {
      this.handleWebSocketErrorResponse(response);
      return;
    }

    if (response.status === WebSocketResponseStatus.OK && response.game) {
      switch (response.messageType) {
        case WebSocketResponseMessageType.PLAYER_KICKED:
          this.handlePlayerKickedMessage(response.game);
          break;
        case WebSocketResponseMessageType.GAME_STARTED:
          this.handleGameStarted(response.game);
          break;
        case WebSocketResponseMessageType.PLAYER_IS_NOW_HOST:
          this.handleAcquiredHost();
          break;
      }

      this.player = this.getCurrentPlayer(response.game);
      this.game.next(response.game);
      this.latestGameState = response.game;
    }
  }

  private handleWebSocketErrorResponse(response: WebSocketResponse): void {
    console.log(response.status);
    let errorMessage: string = this.mapToErrorMessage(response.status);
    this.snackbarService.showMessage(errorMessage);
  }

  private handlePlayerKickedMessage(game: Game): void {
    if (!this.getCurrentPlayer(game)) {
      this.snackbarService.showMessage('You have been kicked from the game');
      this.router.navigateByUrl('/');
    }
  }

  private handleGameStarted(game: Game): void {
    this.router.navigateByUrl(`/games/${game.id}`);
  }

  private handleAcquiredHost() {
    if (this.player) {
      this.player.host = true;
    }
    this.snackbarService.showMessage(
      'The Host hast left the game. You are now the host.'
    );
  }

  private getCurrentPlayer(game: Game): Player | undefined {
    let filteredPlayers: Player[] = game.players.filter(
      (player: Player) => player.id === this.player?.id
    );

    return filteredPlayers.length === 1 ? filteredPlayers[0] : undefined;
  }

  private mapToErrorMessage(responseStatus: WebSocketResponseStatus) {
    switch (responseStatus) {
      case WebSocketResponseStatus.PLAYER_IS_NOT_HOST:
        return 'Only the host of the game can perform this action!';
      case WebSocketResponseStatus.PLAYER_IS_NOT_AT_TURN:
        return 'It is not your turn!';
      case WebSocketResponseStatus.PLAYER_HAS_NOT_ROLLED:
        return 'You first have to roll the dices!';
      case WebSocketResponseStatus.WRONG_TABLE_COLUMN:
        return 'You cannot enter points in the wrong column!';
      case WebSocketResponseStatus.TABLE_CELL_NOT_EMPTY:
        return 'This cell already contains points!';
      case WebSocketResponseStatus.NO_MORE_ROLL:
        return "You don't have any rolls left!";
      default:
        return 'Unknown error occured';
    }
  }
}
