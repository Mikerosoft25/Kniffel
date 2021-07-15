import {Component, Input, OnInit} from '@angular/core';
import {Game, Player, TableCell} from "../../interfaces";
import {WebSocketService} from "../../services/web-socket.service";

@Component({
  selector: 'app-table-player-column',
  templateUrl: './table-player-column.component.html',
  styleUrls: ['./table-player-column.component.scss']
})
export class TablePlayerColumnComponent implements OnInit {

  @Input() game: Game;
  @Input() player: Player;
  @Input() columnPlayer: Player;
  @Input() col: number;

  constructor(private webSocketService: WebSocketService) { }

  ngOnInit(): void {
  }

  cellClicked(row: number) {
    const tableCell: TableCell = {
      row: row,
      col: this.columnPlayer.id
    }

    this.webSocketService.cellClicked(tableCell);
  }

}
