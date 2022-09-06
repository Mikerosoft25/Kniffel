import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Player, TableCell } from 'src/app/models/model';

@Component({
  selector: 'app-game-table',
  templateUrl: './game-table.component.html',
  styleUrls: ['./game-table.component.scss'],
})
export class GameTableComponent implements OnInit {
  @Output() onCellClick: EventEmitter<TableCell> = new EventEmitter();
  @Input() players!: Player[];
  @Input() player!: Player;
  @Input() playerAtTurn!: Player;

  rowDefinitons = [
    'Aces',
    'Twos',
    'Threes',
    'Fours',
    'Fives',
    'Sixes',
    'Total Score',
    'Bonus',
    'Total upper score',
    'Three of a kind',
    'Four of a kind',
    'Full house',
    'Small straight',
    'Large Straight',
    'Kniffel',
    'Chance',
    'Total lower score',
    'Total upper score',
    'Grand Total',
  ];

  constructor() {}

  ngOnInit(): void {}

  cellClicked(row: number, col: number) {
    this.onCellClick.emit({ row: row, column: col });
  }
}
