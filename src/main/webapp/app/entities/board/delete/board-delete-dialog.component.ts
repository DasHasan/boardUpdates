import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoard } from '../board.model';
import { BoardService } from '../service/board.service';

@Component({
  templateUrl: './board-delete-dialog.component.html',
})
export class BoardDeleteDialogComponent {
  board?: IBoard;

  constructor(protected boardService: BoardService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.boardService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
