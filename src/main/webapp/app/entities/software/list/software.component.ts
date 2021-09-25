import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoftware } from '../software.model';
import { SoftwareService } from '../service/software.service';
import { SoftwareDeleteDialogComponent } from '../delete/software-delete-dialog.component';

@Component({
  selector: 'jhi-software',
  templateUrl: './software.component.html',
})
export class SoftwareComponent implements OnInit {
  software?: ISoftware[];
  isLoading = false;

  constructor(protected softwareService: SoftwareService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.softwareService.query().subscribe(
      (res: HttpResponse<ISoftware[]>) => {
        this.isLoading = false;
        this.software = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISoftware): number {
    return item.id!;
  }

  delete(software: ISoftware): void {
    const modalRef = this.modalService.open(SoftwareDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.software = software;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
