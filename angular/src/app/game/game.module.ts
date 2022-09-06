import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameService } from './game.service';
import { GameMenuComponent } from './game-menu/game-menu.component';
import { GameRouterModule } from './game-router/game-router.module';
import { GameListComponent } from './game-list/game-list.component';
import { CreateGameComponent } from './create-game/create-game.component';
import { JoinGameComponent } from './join-game/join-game.component';
import { FormsModule } from '@angular/forms';
import { GameLobbyComponent } from './game-lobby/game-lobby.component';
import { GameMainComponent } from './game-main/game-main.component';
import { RouterModule } from '@angular/router';
import { GameComponent } from './game/game.component';
import { DiceComponent } from './dice/dice.component';
import { GameTableComponent } from './game-table/game-table.component';

@NgModule({
  declarations: [
    GameMenuComponent,
    GameListComponent,
    CreateGameComponent,
    JoinGameComponent,
    GameLobbyComponent,
    GameMainComponent,
    GameComponent,
    DiceComponent,
    GameTableComponent,
  ],
  imports: [CommonModule, FormsModule, RouterModule, GameRouterModule],
  providers: [GameService],
})
export class GameModule {}
