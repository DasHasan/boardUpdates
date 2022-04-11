import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUpdateVersionPrecondition } from '../update-version-precondition.model';

@Component({
  selector: 'jhi-update-version-precondition-detail',
  templateUrl: './update-version-precondition-detail.component.html',
})
export class UpdateVersionPreconditionDetailComponent implements OnInit {
  updateVersionPrecondition: IUpdateVersionPrecondition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ updateVersionPrecondition }) => {
      this.updateVersionPrecondition = updateVersionPrecondition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
