import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoardUpdateSuccessor } from '../board-update-successor.model';
import { BoardUpdateSuccessorService } from '../service/board-update-successor.service';
import { BoardUpdateSuccessorDeleteDialogComponent } from '../delete/board-update-successor-delete-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-board-update-successor',
  templateUrl: './board-update-successor.component.html',
})
export class BoardUpdateSuccessorComponent implements OnInit {
  @Input() boardUpdateId?: number;
  boardUpdateSuccessors?: IBoardUpdateSuccessor[];
  isLoading = false;

  constructor(
    protected boardUpdateSuccessorService: BoardUpdateSuccessorService,
    protected modalService: NgbModal,
    protected router: Router
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };
  }

  loadAll(): void {
    // alert(this.boardUpdateId)
    this.isLoading = true;

    const query = this.boardUpdateSuccessorService.query(this.boardUpdateId ? { 'fromId.equals': this.boardUpdateId } : {});
    query.subscribe(
      (res: HttpResponse<IBoardUpdateSuccessor[]>) => {
        this.isLoading = false;
        this.boardUpdateSuccessors = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBoardUpdateSuccessor): number {
    return item.id!;
  }

  delete(boardUpdateSuccessor: IBoardUpdateSuccessor): void {
    const modalRef = this.modalService.open(BoardUpdateSuccessorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.boardUpdateSuccessor = boardUpdateSuccessor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  clicked(id: number): void {
    this.router.navigate(['/board-update', id, 'view']).then(() => {
      this.boardUpdateSuccessors = [];
    });
  }
}
