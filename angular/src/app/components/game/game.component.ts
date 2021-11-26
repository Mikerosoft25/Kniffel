import { Component, OnInit } from '@angular/core';
import {Game, Player} from "../../interfaces";
import {Subscription} from "rxjs";
import {WebSocketService} from "../../services/web-socket.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  player: Player;
  players: Player[];
  scoreBoard: Player[];
  winner: Player;
  game: Game;

  private gameSubscription: Subscription;
  private routeSub: Subscription;

  constructor(
    private webSocketService: WebSocketService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.gameSubscription = this.webSocketService.game.subscribe((game: Game) => {
      this.game = game;
      this.players = game.players;

      if (this.game.ended) {
        this.scoreBoard = [];

        // creating copies of the players
        this.players.forEach((player) => {
          this.scoreBoard.push(JSON.parse(JSON.stringify(player)))
        })

        // sorting the scoreboard
        this.scoreBoard.sort((a, b) => b.points[18] - a.points[18] );

        this.winner = this.scoreBoard[0];
      } else if (this.game.restarted) {
        this.router.navigate(['lobby', this.game.id]);
      }

    })

    this.player = this.webSocketService.getPlayer();

    this.webSocketService.loadGame();
  }

  ngOnDestroy(): void {
    this.gameSubscription.unsubscribe();
  }

  rollDices() {
    this.webSocketService.rollDices();
  }

  backToLobby() {
    this.webSocketService.restartGame();
  }

}
