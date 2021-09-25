import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBoard } from '../board.model';
import { BoardService } from '../service/board.service';
import { BoardDeleteDialogComponent } from '../delete/board-delete-dialog.component';

@Component({
  selector: 'jhi-board',
  templateUrl: './board.component.html',
})
export class BoardComponent implements OnInit {
  boards?: IBoard[];
  isLoading = false;

  constructor(protected boardService: BoardService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.boardService.query().subscribe(
      (res: HttpResponse<IBoard[]>) => {
        this.isLoading = false;
        this.boards = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBoard): number {
    return item.id!;
  }

  delete(board: IBoard): void {
    const modalRef = this.modalService.open(BoardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.board = board;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
