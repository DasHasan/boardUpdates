import {Component, Input, OnInit} from '@angular/core';
import {UpdateKeysComponent} from "app/entities/update-keys/list/update-keys.component";
import {IUpdatePrecondition} from "app/entities/update-precondition/update-precondition.model";

@Component({
  selector: 'jhi-lwi-update-keys',
  templateUrl: './lwi-update-keys.component.html',
})
export class LwiUpdateKeysComponent extends UpdateKeysComponent implements OnInit {
  @Input() updatePrecondition: IUpdatePrecondition | undefined;

  loadAll(): void {
    // super.loadAll();
  }

  ngOnInit(): void {
    if (this.updatePrecondition?.updateKeys) {
      this.updateKeys = this.updatePrecondition.updateKeys
    }
  }
}
