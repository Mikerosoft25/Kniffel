import { Injectable } from '@angular/core';
import * as SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import { Subject } from "rxjs";
import { Dice, Game, Player, TableCell } from "../interfaces";
import { v4 as uuidv4 } from "uuid";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  public snackbarMessage: Subject<string> = new Subject<string>();
  public game: Subject<Game> = new Subject<Game>();
  public gameId: number;
  public player: Player;
  private UUID: any;
  private API_URL = environment.API_URL;

  private stompClient : any = null;

  constructor() {
    this.UUID = uuidv4();
    this.connect();
  }

  ngOnInit(): void {
  }

  connect(): void {
    const socket = new SockJS(this.API_URL);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, function (frame: any) {
      console.log('Connected: ' + frame);
    });
  }

  disconnect(): void {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
  }


  createGame(name: string) : void {

    let subscription = this.stompClient.subscribe('/topic/game/create', (response: any) => {
      let gameResponse: Game = JSON.parse(response.body).content;
      this.game.next(gameResponse);
      this.gameId = gameResponse.id;

      subscription.unsubscribe();

      this.player = {
        name: name,
        gameId: gameResponse.id,
        host: true,
        id: 0
      }

      this.subscribeToGame(gameResponse.id);
    });

    this.stompClient.send("/app/game/create", {}, JSON.stringify({"player": { "name": name }}));

    // const observable = new Observable(subscriber => {
    //   this.stompClient.subscribe('/topic/game/create', (response: any) => {
    //       subscriber.next(JSON.parse(response.body).content);
    //   });
    // });
    //
    // this.stompClient.send("/app/game/create", {}, JSON.stringify({"name": name}));
    //
    // return observable;
  }

  joinGame(name: string, gameId: number): void {

    this.gameId = gameId;
    this.subscribeToGame(gameId);

    this.player = {
      name: name,
      gameId: gameId,
      host: false
    }

    this.stompClient.send("/app/game/join/" + this.gameId, {}, JSON.stringify({ "player": this.player }));
  }

  subscribeToGame(gameId: number): void {
    this.stompClient.subscribe('/topic/game/' + gameId, (jsonResponse: any) => {

      let response = JSON.parse(jsonResponse.body);

      if (response.statusMessage == "OK") {
        let gameResponse: Game = response.content;

        if (this.player.id == undefined) {
          this.player.id = gameResponse.players.length - 1;
        }

        for (let player of gameResponse.players) {
          if (player.id == this.player.id && player.host) {
            this.player.host = true;
          }
        }

        this.game.next(gameResponse);

        console.log(gameResponse);

      } else {
          if (response.targetUUID === this.UUID) {
            let message: string = this.handleStatusMessage(response.statusMessage);
            this.snackbarMessage.next(message);
          }
      }

    });
  }

  loadGame(): void {
    this.stompClient.send("/app/game/" + this.gameId, {}, JSON.stringify({}));
  }

  startGame(): void {
    this.stompClient.send("/app/game/start/" + this.gameId, {}, JSON.stringify({ "player": this.player, "requesterUUID": this.UUID }));
  }

  getPlayer() : Player {
    return this.player;
  }

  clickDice(dice: Dice): void {
    this.stompClient.send("/app/game/dice/click/" + this.gameId, {}, JSON.stringify({ "player": this.player, "dice": dice, "requesterUUID": this.UUID }));
  }

  rollDices(): void {
    this.stompClient.send("/app/game/dice/roll/" + this.gameId, {}, JSON.stringify({ "player": this.player, "requesterUUID": this.UUID }));
  }

  cellClicked(tableCell: TableCell): void {
    this.stompClient.send("/app/game/cell/click/" + this.gameId, {}, JSON.stringify({ "player": this.player, "tableCell": tableCell, "requesterUUID": this.UUID }));
  }

  restartGame() {
    this.stompClient.send("/app/game/restart/" + this.gameId, {}, JSON.stringify({ "player": this.player, "requesterUUID": this.UUID }))
  }

  handleStatusMessage(statusMessage: string) {
    let message: string = "ERROR_NO_MESSAGE";

    if (statusMessage === "GAME_NOT_FOUND") {
      message = "Das Spiel konnte nicht gefunden werden!"
    } else if (statusMessage === "WRONG_PLAYER_DICE_ROLL") {
      message = "Du kannst nicht würfeln, da du nicht dran bist!";
    } else if (statusMessage === "WRONG_PLAYER_DICE_CLICK") {
      message = "Du kannst keine Würfel sperren, da du nicht dran bist!";
    } else if (statusMessage === "WRONG_PLAYER_CELL_CLICK") {
      message = "Du kannst keine Punkte eintragen, da du nicht dran bist!";
    } else if (statusMessage === "WRONG_COLUMN_CLICK") {
      message = "Du kannst Punkte nur in deiner Spalte eintragen!";
    } else if (statusMessage === "NO_MORE_ROLLS") {
      message = "Das Spiel ist bereits vorbei!";
    } else if (statusMessage === "GAME_ENDED") {
      message = "Das Spiel ist bereits vorbei!";
    } else if (statusMessage === "GAME_ALREADY_STARTED") {
      message = "Du kannst dem Spiel nicht beitreten, da es bereits gestartet hat!";
    } else if (statusMessage === "DICE_NOT_ROLLED") {
      message = "Du musst erst Würfeln!";
    } else if (statusMessage === "NOT_HOST_TRIES_RESTART") {
      message = "Nur der Host kann zur Lobby zurückkehren!";
    } else if (statusMessage === "CELL_NOT_EMPTY") {
      message = "Wähle ein freies Feld um Punkte einzutragen!";
    } else if (statusMessage === "NOT_HOST_TRIES_START") {
      message = "Nur der Host kann das Spiel starten!";
    } else if (statusMessage == "PLAYER_NEEDS_TO_ROLL")  {
      message = "Du musst erst würfeln bevor du Punkte eintragen kannst!";
    }

    return message;
  }
}
