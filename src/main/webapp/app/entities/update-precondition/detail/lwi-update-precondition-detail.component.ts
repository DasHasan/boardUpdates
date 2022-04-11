import {Component, Input, OnInit} from '@angular/core';

import {IUpdatePrecondition} from '../update-precondition.model';
import {
  UpdatePreconditionDetailComponent
} from "app/entities/update-precondition/detail/update-precondition-detail.component";

@Component({
  selector: 'jhi-lwi-update-precondition-detail',
  templateUrl: './lwi-update-precondition-detail.component.html',
})
export class LwiUpdatePreconditionDetailComponent extends UpdatePreconditionDetailComponent implements OnInit {
  @Input() precondition: IUpdatePrecondition | null = null;

  ngOnInit(): void {
    super.ngOnInit();
    this.updatePrecondition = this.precondition;
  }
}
