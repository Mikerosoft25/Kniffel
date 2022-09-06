import { Component, OnInit } from '@angular/core';
import { SnackbarService } from './snackbar.service';

@Component({
  selector: 'app-snackbar',
  templateUrl: './snackbar.component.html',
  styleUrls: ['./snackbar.component.scss'],
})
export class SnackbarComponent implements OnInit {
  showSnackbar: boolean = false;
  message: string = '';

  constructor(private snackbarServie: SnackbarService) {}

  ngOnInit(): void {
    this.snackbarServie.snackbarMessage.subscribe((message: string) => {
      this.showSnackbar = true;
      this.message = message;
    });
  }

  hideSnackbar() {
    this.showSnackbar = false;
  }
}
