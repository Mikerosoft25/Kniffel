import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { MainMenuComponent } from './components/main-menu/main-menu.component';
import {RouterModule} from "@angular/router";
import { LobbyComponent } from './components/lobby/lobby.component';
import { PlayerListComponent } from './components/player-list/player-list.component';
import { GameComponent } from './components/game/game.component';
import { DiceComponent } from './components/dice/dice.component';
import { TableDefinitionColumnComponent } from './components/table-definition-column/table-definition-column.component';
import { TablePlayerColumnComponent } from './components/table-player-column/table-player-column.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatSnackBarModule} from "@angular/material/snack-bar";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MainMenuComponent,
    LobbyComponent,
    PlayerListComponent,
    GameComponent,
    DiceComponent,
    TableDefinitionColumnComponent,
    TablePlayerColumnComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    RouterModule.forRoot([
      { path: '', component: MainMenuComponent },
      { path: 'lobby/:gameId', component: LobbyComponent },
      { path: 'game/:gameId', component: GameComponent }
    ]),
    BrowserAnimationsModule,
    MatSnackBarModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
