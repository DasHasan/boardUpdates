import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGroup } from '../group.model';
import { GroupService } from '../service/group.service';
import { GroupDeleteDialogComponent } from '../delete/group-delete-dialog.component';

@Component({
  selector: 'jhi-group',
  templateUrl: './group.component.html',
})
export class GroupComponent implements OnInit {
  groups?: IGroup[];
  isLoading = false;

  constructor(protected groupService: GroupService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.groupService.query().subscribe(
      (res: HttpResponse<IGroup[]>) => {
        this.isLoading = false;
        this.groups = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGroup): number {
    return item.id!;
  }

  delete(group: IGroup): void {
    const modalRef = this.modalService.open(GroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.group = group;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
