import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';

@Component({
  templateUrl: './board-update-delete-dialog.component.html',
})
export class BoardUpdateDeleteDialogComponent {
  boardUpdate?: IBoardUpdate;

  constructor(protected boardUpdateService: BoardUpdateService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.boardUpdateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
