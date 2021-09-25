import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFirmware } from '../firmware.model';

@Component({
  selector: 'jhi-firmware-detail',
  templateUrl: './firmware-detail.component.html',
})
export class FirmwareDetailComponent implements OnInit {
  firmware: IFirmware | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ firmware }) => {
      this.firmware = firmware;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
