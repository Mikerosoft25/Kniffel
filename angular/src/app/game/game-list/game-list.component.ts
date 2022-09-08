import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Game } from 'src/app/models/model';
import { GameService } from '../game.service';

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss'],
})
export class GameListComponent implements OnInit {
  @Input() games: Game[] = [];
  @Output() joinGameId = new EventEmitter<string>();

  refreshImagePath: string = "./assets/images/refresh-icon.svg";
  refreshing: boolean = false;

  constructor(private gameService: GameService) {}

  ngOnInit(): void {}

  joinGame(game: Game) {
    this.joinGameId.emit(game.id);
  }

  refreshGameList() {
    this.refreshing = true;
    this.gameService.listGames().subscribe((games: Game[]) => {
      this.games = [...games];
      this.refreshing = false;
    });
  }
}
