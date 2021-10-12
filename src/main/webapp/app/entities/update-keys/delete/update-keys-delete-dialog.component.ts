import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUpdateKeys } from '../update-keys.model';
import { UpdateKeysService } from '../service/update-keys.service';

@Component({
  templateUrl: './update-keys-delete-dialog.component.html',
})
export class UpdateKeysDeleteDialogComponent {
  updateKeys?: IUpdateKeys;

  constructor(protected updateKeysService: UpdateKeysService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.updateKeysService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
