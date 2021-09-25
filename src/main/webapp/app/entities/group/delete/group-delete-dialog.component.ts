import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGroup } from '../group.model';
import { GroupService } from '../service/group.service';

@Component({
  templateUrl: './group-delete-dialog.component.html',
})
export class GroupDeleteDialogComponent {
  group?: IGroup;

  constructor(protected groupService: GroupService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.groupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
