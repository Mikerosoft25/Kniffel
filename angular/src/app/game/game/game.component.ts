import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Game, GameStatus, Player, TableCell} from 'src/app/models/model';
import {GameService} from '../game.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss'],
})
export class GameComponent implements OnInit {
  gameStatus = GameStatus;
  game!: Game;
  player!: Player;
  playerAtTurn!: Player;
  scoreboard: String[] | undefined = ["Hans", "Franz", "Dampg"];

  constructor(private gameService: GameService, private router: Router) {
  }

  ngOnInit(): void {
    if (
      this.gameService.player === undefined ||
      this.gameService.latestGameState === undefined
    ) {
      this.router.navigate(['']);
      return;
    }

    this.game = this.gameService.latestGameState;
    this.player = this.gameService.player;
    this.playerAtTurn = this.game.players[this.game.playerTurn ?? 0];
    this.gameService.game.subscribe((game: Game) => {
      this.game = game;
      this.playerAtTurn = this.game.players[this.game.playerTurn ?? 0];

      if (this.game.status === GameStatus.ENDED) {
        this.scoreboard = this.gameService.determineScoreboard(game);
      }
    });
  }

  rollDice(gameId: string) {
    this.gameService.rollDice(gameId);
  }

  changeDiceLock(gameId: string, diceIndex: number) {
    this.gameService.changeDiceLock(gameId, diceIndex);
  }

  clickTableCell(gameId: string, tableCell: TableCell) {
    this.gameService.clickTableCell(gameId, tableCell);
  }

  returnToLobby(gameId: string) {
    this.gameService.resetGame(gameId);
    this.router.navigate(['games', gameId, 'lobby']);
  }
}
