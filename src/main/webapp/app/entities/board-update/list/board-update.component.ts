import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';
import { BoardUpdateDeleteDialogComponent } from '../delete/board-update-delete-dialog.component';
import { IBoard } from 'app/entities/board/board.model';

@Component({
  selector: 'jhi-board-update-list',
  templateUrl: './board-update.component.html',
})
export class BoardUpdateComponent implements OnInit {
  @Input() board?: IBoard;
  boardUpdates?: IBoardUpdate[];
  isLoading = false;

  constructor(protected boardUpdateService: BoardUpdateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.boardUpdateService.query().subscribe(
      (res: HttpResponse<IBoardUpdate[]>) => {
        this.isLoading = false;
        this.boardUpdates = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBoardUpdate): number {
    return item.id!;
  }

  delete(boardUpdate: IBoardUpdate): void {
    const modalRef = this.modalService.open(BoardUpdateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.boardUpdate = boardUpdate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
