import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoardUpdateSuccessor } from '../board-update-successor.model';
import { BoardUpdateSuccessorService } from '../service/board-update-successor.service';

@Component({
  templateUrl: './board-update-successor-delete-dialog.component.html',
})
export class BoardUpdateSuccessorDeleteDialogComponent {
  boardUpdateSuccessor?: IBoardUpdateSuccessor;

  constructor(protected boardUpdateSuccessorService: BoardUpdateSuccessorService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.boardUpdateSuccessorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
