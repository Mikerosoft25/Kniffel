import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Game } from 'src/app/models/model';

@Component({
  selector: 'app-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss'],
})
export class GameListComponent implements OnInit {
  @Input() games: Game[] = [];
  @Output() joinGameId = new EventEmitter<string>();

  constructor() {}

  ngOnInit(): void {}

  joinGame(game: Game) {
    this.joinGameId.emit(game.id);
  }
}
