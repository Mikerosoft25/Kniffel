import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Game } from 'src/app/models/model';
import { SnackbarService } from 'src/app/snackbar/snackbar.service';
import { GameService } from '../game.service';

@Component({
  selector: 'app-create-game',
  templateUrl: './create-game.component.html',
  styleUrls: ['./create-game.component.scss'],
})
export class CreateGameComponent implements OnInit {
  playerName: string | undefined;

  constructor(
    private gameService: GameService,
    private router: Router,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {}

  createGame() {
    if (this.playerName) {
      this.gameService.createGame(this.playerName).subscribe({
        next: (game: Game) => {
          this.gameService.game.next(game);
          this.gameService.player = game.players[0];
          this.router.navigate(['games', game.id, 'lobby']);
        },
        error: (error: any) => {
          this.snackbarService.showMessage(
            `An errror occured: ${error.error.message}`
          );
        },
      });
    }
  }
}
