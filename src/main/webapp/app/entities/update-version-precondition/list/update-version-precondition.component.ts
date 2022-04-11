import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUpdateVersionPrecondition } from '../update-version-precondition.model';
import { UpdateVersionPreconditionService } from '../service/update-version-precondition.service';
import { UpdateVersionPreconditionDeleteDialogComponent } from '../delete/update-version-precondition-delete-dialog.component';

@Component({
  selector: 'jhi-update-version-precondition',
  templateUrl: './update-version-precondition.component.html',
})
export class UpdateVersionPreconditionComponent implements OnInit {
  updateVersionPreconditions?: IUpdateVersionPrecondition[];
  isLoading = false;

  constructor(protected updateVersionPreconditionService: UpdateVersionPreconditionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.updateVersionPreconditionService.query().subscribe(
      (res: HttpResponse<IUpdateVersionPrecondition[]>) => {
        this.isLoading = false;
        this.updateVersionPreconditions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUpdateVersionPrecondition): number {
    return item.id!;
  }

  delete(updateVersionPrecondition: IUpdateVersionPrecondition): void {
    const modalRef = this.modalService.open(UpdateVersionPreconditionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.updateVersionPrecondition = updateVersionPrecondition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
