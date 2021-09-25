import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoftwareUpdate } from '../software-update.model';
import { SoftwareUpdateService } from '../service/software-update.service';
import { SoftwareUpdateDeleteDialogComponent } from '../delete/software-update-delete-dialog.component';

@Component({
  selector: 'jhi-software-update',
  templateUrl: './software-update.component.html',
})
export class SoftwareUpdateComponent implements OnInit {
  softwareUpdates?: ISoftwareUpdate[];
  isLoading = false;

  constructor(protected softwareUpdateService: SoftwareUpdateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.softwareUpdateService.query().subscribe(
      (res: HttpResponse<ISoftwareUpdate[]>) => {
        this.isLoading = false;
        this.softwareUpdates = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISoftwareUpdate): number {
    return item.id!;
  }

  delete(softwareUpdate: ISoftwareUpdate): void {
    const modalRef = this.modalService.open(SoftwareUpdateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.softwareUpdate = softwareUpdate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
