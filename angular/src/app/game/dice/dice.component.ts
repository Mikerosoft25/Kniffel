import { Component, Input, OnInit } from '@angular/core';
import { Dice } from 'src/app/models/model';

@Component({
  selector: 'app-dice',
  templateUrl: './dice.component.html',
  styleUrls: ['./dice.component.scss'],
})
export class DiceComponent implements OnInit {
  private IMAGES_PATH: string = './assets/images';
  imageSrc: string = this.IMAGES_PATH + '/dice_1.png';

  @Input() dice!: Dice;
  @Input() enabled!: boolean;

  constructor() {}

  ngOnInit(): void {
    this.imageSrc = `${this.IMAGES_PATH}/dice_${this.dice.value}.png`;
  }
}
