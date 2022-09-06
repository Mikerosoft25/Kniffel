import { Component, OnDestroy, OnInit } from '@angular/core';
import { Game } from 'src/app/models/model';
import { GameService } from '../game.service';

@Component({
  selector: 'app-game-menu',
  templateUrl: './game-menu.component.html',
  styleUrls: ['./game-menu.component.scss'],
})
export class GameMenuComponent implements OnDestroy {
  private readonly REQUEST_INTERVAL: number = 20 * 1000;
  private interval: number;

  games: Game[] = [];
  joinGameId: string = "";

  constructor(private gameService: GameService) {
    this.gameService.reset();

    const getGames = () => {
      this.gameService.listGames().subscribe((games: Game[]) => {
        this.games = [...games];
      });
    };

    getGames();
    this.interval = window.setInterval(() => {
      getGames();
    }, this.REQUEST_INTERVAL);
  }

  changeJoinGameId(gameId: string) {
    this.joinGameId = gameId;
  }

  ngOnDestroy(): void {
    clearInterval(this.interval);
  }
}
