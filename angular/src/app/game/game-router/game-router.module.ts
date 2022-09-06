import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { GameMenuComponent } from '../game-menu/game-menu.component';
import { GameLobbyComponent } from '../game-lobby/game-lobby.component';
import { GameMainComponent } from '../game-main/game-main.component';
import { GameComponent } from '../game/game.component';

const routes: Routes = [
  {
    path: 'games',
    component: GameMainComponent,
    children: [
      { path: '', pathMatch: 'full', component: GameMenuComponent },
      { path: ':gameId/lobby', component: GameLobbyComponent },
      { path: ':gameId', component: GameComponent}
    ],
  },
];

@NgModule({
  declarations: [],
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class GameRouterModule {}
