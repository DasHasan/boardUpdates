import {Component, OnInit} from '@angular/core';
import {UpdateKeysUpdateComponent} from "app/entities/update-keys/update/update-keys-update.component";
import {UpdateKeys} from "app/entities/update-keys/update-keys.model";
import {UpdatePrecondition} from "app/entities/update-precondition/update-precondition.model";

@Component({
  selector: 'jhi-update-keys-update',
  templateUrl: './lwi-update-keys-update.component.html',
})
export class LwiUpdateKeysUpdateComponent extends UpdateKeysUpdateComponent implements OnInit {

  ngOnInit(): void {
    // eslint-disable-next-line no-console
    this.activatedRoute.queryParams.subscribe(({updatePreconditionId}) => {
      const updateKeys = new UpdateKeys();
      const updatePrecondition = new UpdatePrecondition();
      updatePrecondition.id = updatePreconditionId
      updateKeys.updatePrecondition = updatePrecondition
      this.updateForm(updateKeys)
    })
  }
}
