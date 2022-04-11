import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUpdateKeys } from '../update-keys.model';
import { UpdateKeysService } from '../service/update-keys.service';
import { UpdateKeysDeleteDialogComponent } from '../delete/update-keys-delete-dialog.component';

@Component({
  selector: 'jhi-update-keys',
  templateUrl: './update-keys.component.html',
})
export class UpdateKeysComponent implements OnInit {
  updateKeys?: IUpdateKeys[];
  isLoading = false;

  constructor(protected updateKeysService: UpdateKeysService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.updateKeysService.query().subscribe(
      (res: HttpResponse<IUpdateKeys[]>) => {
        this.isLoading = false;
        this.updateKeys = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUpdateKeys): number {
    return item.id!;
  }

  delete(updateKeys: IUpdateKeys): void {
    const modalRef = this.modalService.open(UpdateKeysDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.updateKeys = updateKeys;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
