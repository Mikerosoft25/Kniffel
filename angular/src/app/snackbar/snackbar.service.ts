import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SnackbarService {
  snackbarMessage: Subject<string> = new Subject<string>();

  constructor() {}

  showMessage(message: string) {
    this.snackbarMessage.next(message);
  }
}
