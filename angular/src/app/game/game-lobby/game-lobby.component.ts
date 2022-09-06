import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Game, Player } from 'src/app/models/model';
import { GameService } from '../game.service';

@Component({
  selector: 'app-game-lobby',
  templateUrl: './game-lobby.component.html',
  styleUrls: ['./game-lobby.component.scss'],
})
export class GameLobbyComponent implements OnInit {
  game: Game | undefined;
  player!: Player;

  constructor(
    private gameService: GameService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.gameService.player === undefined) {
      this.router.navigate(['']);
      return;
    }

    this.player = this.gameService.player;
    this.gameService.game.subscribe((game: Game) => {
      this.game = game;
    });

    if (!this.gameService.isConnected()) {
      let gameId = this.route.snapshot.params['gameId'];
      this.gameService.connectToGame(gameId);
    }
  }

  kickPlayer(player: Player) {
    if (!this.game) {
      return;
    }
    this.gameService.kickPlayer(player.id, this.game.id);
  }

  startGame(gameId: string) {
    this.gameService.startGame(gameId);
  }
}
