import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUpdatePrecondition } from '../update-precondition.model';
import { UpdatePreconditionService } from '../service/update-precondition.service';
import { UpdatePreconditionDeleteDialogComponent } from '../delete/update-precondition-delete-dialog.component';

@Component({
  selector: 'jhi-update-precondition',
  templateUrl: './update-precondition.component.html',
})
export class UpdatePreconditionComponent implements OnInit {
  updatePreconditions?: IUpdatePrecondition[];
  isLoading = false;

  constructor(protected updatePreconditionService: UpdatePreconditionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.updatePreconditionService.query().subscribe(
      (res: HttpResponse<IUpdatePrecondition[]>) => {
        this.isLoading = false;
        this.updatePreconditions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUpdatePrecondition): number {
    return item.id!;
  }

  delete(updatePrecondition: IUpdatePrecondition): void {
    const modalRef = this.modalService.open(UpdatePreconditionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.updatePrecondition = updatePrecondition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
