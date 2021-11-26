import {Component, Input, OnInit} from '@angular/core';
import {Player} from "../../interfaces";
import {WebSocketService} from "../../services/web-socket.service";

@Component({
  selector: 'app-player-list',
  templateUrl: './player-list.component.html',
  styleUrls: ['./player-list.component.scss']
})
export class PlayerListComponent implements OnInit {

  @Input() player: Player;

  constructor() {}

  ngOnInit(): void {
  }

}
