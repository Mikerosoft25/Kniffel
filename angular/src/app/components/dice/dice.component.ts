import {Component, Input, OnInit} from '@angular/core';
import {Dice, Game, Player} from "../../interfaces";
import {WebSocketService} from "../../services/web-socket.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-dice',
  templateUrl: './dice.component.html',
  styleUrls: ['./dice.component.scss']
})
export class DiceComponent implements OnInit {

  imagePath: string = "../../../assets/images/";
  imageSrc: string = this.imagePath + "dice_1.png";
  @Input() dice: Dice;
  @Input() game: Game;
  @Input() player: Player;

  private gameSubscription: Subscription;

  constructor(private webSocketService: WebSocketService) { }

  ngOnInit(): void {
    this.gameSubscription = this.webSocketService.game.subscribe((game: Game) => {
      this.game = game;

      game.dices.forEach(dice => {
        if (dice.index == this.dice.index) {
          this.dice = dice;
        }
      });

      this.imageSrc = this.imagePath + "dice_" + this.dice.value + ".png";
    })
  }

  clickDice() {
    this.webSocketService.clickDice(this.dice);
  }

}
