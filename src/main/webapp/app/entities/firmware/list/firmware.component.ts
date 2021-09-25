import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFirmware } from '../firmware.model';
import { FirmwareService } from '../service/firmware.service';
import { FirmwareDeleteDialogComponent } from '../delete/firmware-delete-dialog.component';

@Component({
  selector: 'jhi-firmware',
  templateUrl: './firmware.component.html',
})
export class FirmwareComponent implements OnInit {
  firmware?: IFirmware[];
  isLoading = false;

  constructor(protected firmwareService: FirmwareService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.firmwareService.query().subscribe(
      (res: HttpResponse<IFirmware[]>) => {
        this.isLoading = false;
        this.firmware = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFirmware): number {
    return item.id!;
  }

  delete(firmware: IFirmware): void {
    const modalRef = this.modalService.open(FirmwareDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.firmware = firmware;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
