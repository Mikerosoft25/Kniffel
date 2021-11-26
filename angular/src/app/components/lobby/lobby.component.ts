import { Component, OnInit } from '@angular/core';
import {Game, Player} from "../../interfaces";
import {WebSocketService} from "../../services/web-socket.service";
import {Subscription} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-lobby',
  templateUrl: './lobby.component.html',
  styleUrls: ['./lobby.component.scss']
})
export class LobbyComponent implements OnInit {

  player: Player;
  players: Player[];
  game: Game;

  private gameSubscription: Subscription;
  private routeSub: Subscription;

  constructor(
    private webSocketService: WebSocketService,
    private route: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit(): void {
    this.gameSubscription = this.webSocketService.game.subscribe((game: Game) => {
      this.game = game;
      this.players = game.players;

      console.log("game", game);

      if (this.game.started) {
        console.log("started");
        this.router.navigate(['game', game.id]);
      }
    });

    this.player = this.webSocketService.getPlayer();

    this.webSocketService.loadGame();
    // this.routeSub = this.route.params.subscribe(params => {
    //   this.webSocketService.loadGame(params['gameId']);
    // });

  }

  ngOnDestroy(): void {
    this.gameSubscription.unsubscribe();
  }

  startGame() {
    this.webSocketService.startGame();
  }
}
