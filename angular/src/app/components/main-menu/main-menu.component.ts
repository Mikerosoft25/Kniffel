import {Component, Inject, OnInit} from '@angular/core';
import {WebSocketService} from "../../services/web-socket.service";
import {Router} from "@angular/router";
import { Game } from "../../interfaces";
import {Observable, Subscription} from "rxjs";

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})

export class MainMenuComponent implements OnInit {
  name: string;
  gameId: number;

  private gameSubscription: Subscription;

  constructor(private webSocketService: WebSocketService, private router: Router) {
  }

  ngOnInit(): void {
    this.gameSubscription = this.webSocketService.game.subscribe((game: Game) => {
      this.router.navigate(['lobby', game.id]);
    });
  }

  ngOnDestroy(): void {
    this.gameSubscription.unsubscribe();
  }


  createGame() {

    this.webSocketService.createGame(this.name);

    // this.webSocketService.createGame("test").subscribe((res: any) => console.log(res));

    // this.webSocketService.game.subscribe({
    //   next: (v) => console.log(v)
    // });

    // let game: Game;
    //
    // let subscription = this.webSocketService.getStompClient().subscribe('/topic/game/create', (response: any) => {
    //   game = JSON.parse(response.body).content;
    //
    //   subscription.unsubscribe();
    //
    //   this.router.navigate(['lobby', game.id]);
    // });
    //
    // this.webSocketService.getStompClient().send("/app/game/create", {}, JSON.stringify({"name": this.name}));

  }

  joinGame() {
    this.webSocketService.joinGame(this.name, this.gameId);
  }

  enterPressed() {
    console.log(this.name, this.gameId);
    if (this.gameId) {
      this.joinGame();
    } else {
      this.createGame();
    }
  }
}
