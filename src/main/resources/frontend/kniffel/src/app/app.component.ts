import { Component } from '@angular/core';
import { WebSocketService } from "./services/web-socket.service";
import {MatSnackBar, MatSnackBarConfig} from "@angular/material/snack-bar";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'kniffel';

  constructor(private webSocketService: WebSocketService, private snackBar: MatSnackBar) {
  }

  ngOnDestroy() {
  }

  ngOnInit() {
    this.webSocketService.snackbarMessage.subscribe(message => {
      this.openSnackBar(message);
    });
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, "OK", {
      panelClass: 'snackbar',
      // duration: 3000
    });
  }
}
