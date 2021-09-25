import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFirmwareUpdate } from '../firmware-update.model';

@Component({
  selector: 'jhi-firmware-update-detail',
  templateUrl: './firmware-update-detail.component.html',
})
export class FirmwareUpdateDetailComponent implements OnInit {
  firmwareUpdate: IFirmwareUpdate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ firmwareUpdate }) => {
      this.firmwareUpdate = firmwareUpdate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
