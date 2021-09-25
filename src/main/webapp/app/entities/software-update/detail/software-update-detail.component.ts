import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISoftwareUpdate } from '../software-update.model';

@Component({
  selector: 'jhi-software-update-detail',
  templateUrl: './software-update-detail.component.html',
})
export class SoftwareUpdateDetailComponent implements OnInit {
  softwareUpdate: ISoftwareUpdate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ softwareUpdate }) => {
      this.softwareUpdate = softwareUpdate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
