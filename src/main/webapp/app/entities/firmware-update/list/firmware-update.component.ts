import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFirmwareUpdate } from '../firmware-update.model';
import { FirmwareUpdateService } from '../service/firmware-update.service';
import { FirmwareUpdateDeleteDialogComponent } from '../delete/firmware-update-delete-dialog.component';

@Component({
  selector: 'jhi-firmware-update',
  templateUrl: './firmware-update.component.html',
})
export class FirmwareUpdateComponent implements OnInit {
  firmwareUpdates?: IFirmwareUpdate[];
  isLoading = false;

  constructor(protected firmwareUpdateService: FirmwareUpdateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.firmwareUpdateService.query().subscribe(
      (res: HttpResponse<IFirmwareUpdate[]>) => {
        this.isLoading = false;
        this.firmwareUpdates = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFirmwareUpdate): number {
    return item.id!;
  }

  delete(firmwareUpdate: IFirmwareUpdate): void {
    const modalRef = this.modalService.open(FirmwareUpdateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.firmwareUpdate = firmwareUpdate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
