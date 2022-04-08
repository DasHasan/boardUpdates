import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUpdatePrecondition } from '../update-precondition.model';

@Component({
  selector: 'jhi-update-precondition-detail',
  templateUrl: './update-precondition-detail.component.html',
})
export class UpdatePreconditionDetailComponent implements OnInit {
  updatePrecondition: IUpdatePrecondition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ updatePrecondition }) => {
      this.updatePrecondition = updatePrecondition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
