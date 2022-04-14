import {Component, Input, OnInit} from '@angular/core';
import {UpdateKeysComponent} from "app/entities/update-keys/list/update-keys.component";
import {IUpdatePrecondition} from "app/entities/update-precondition/update-precondition.model";
import {HttpResponse} from "@angular/common/http";
import {IUpdateKeys} from "app/entities/update-keys/update-keys.model";

@Component({
  selector: 'jhi-lwi-update-keys',
  templateUrl: './lwi-update-keys.component.html',
})
export class LwiUpdateKeysComponent extends UpdateKeysComponent implements OnInit {
  @Input() updatePrecondition: IUpdatePrecondition | undefined;

  loadAll(): void {
    // super.loadAll();

    this.isLoading = true;

    this.updateKeysService.query({'updatePreconditionId.equals': this.updatePrecondition?.id}).subscribe(
      (res: HttpResponse<IUpdateKeys[]>) => {
        this.isLoading = false;
        this.updateKeys = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  // ngOnInit(): void {
  //   if (this.updatePrecondition?.updateKeys) {
  //     this.updateKeys = this.updatePrecondition.updateKeys
  //   }
  // }
}
