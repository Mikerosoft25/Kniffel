import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ErrorDTO, Game } from 'src/app/models/model';
import { SnackbarService } from 'src/app/snackbar/snackbar.service';
import { GameService } from '../game.service';

@Component({
  selector: 'app-join-game',
  templateUrl: './join-game.component.html',
  styleUrls: ['./join-game.component.scss'],
})
export class JoinGameComponent implements OnInit {
  playerName: string = "";
  @Input() gameId!: string;

  constructor(
    private gameService: GameService,
    private router: Router,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {}

  joinGame(): void {
    if (this.playerName && this.gameId) {
      this.gameService.joinGame(this.playerName, this.gameId).subscribe({
        next: (game: Game) => {
          this.gameService.game.next(game);
          this.gameService.player = game.players[game.players.length - 1];
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
