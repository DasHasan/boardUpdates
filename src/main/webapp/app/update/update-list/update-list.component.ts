import { Component, OnInit } from '@angular/core';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';
import { BoardUpdateDeleteDialogComponent } from 'app/entities/board-update/delete/board-update-delete-dialog.component';
import { IBoard } from 'app/entities/board/board.model';
import { FormBuilder } from '@angular/forms';
import { map } from 'rxjs/operators';
import { BoardService } from 'app/entities/board/service/board.service';

@Component({
  selector: 'jhi-update-list',
  templateUrl: './update-list.component.html',
  styleUrls: ['./update-list.component.scss'],
})
export class UpdateListComponent implements OnInit {
  boardUpdates?: IBoardUpdate[];
  isLoading = false;
  filter = '';
  boardsCollection: IBoard[] = [];

  searchForm = this.fb.group({
    id: [],
    version: [],
    path: [],
    type: [],
    releaseDate: [],
    board: [],
  });

  constructor(
    private fb: FormBuilder,
    private boardService: BoardService,
    private boardUpdateService: BoardUpdateService,
    private modalService: NgbModal
  ) {}

  loadAll(): void {
    this.isLoading = true;

    const query = this.getQueryFromForm();
    this.boardUpdateService.query(query).subscribe(
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
    this.loadBoardsCollection();
    this.loadAll();
  }

  trackId(index: number, item: IBoardUpdate): number {
    return item.id!;
  }

  trackBoardById(index: number, item: IBoard): number {
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

  protected loadBoardsCollection(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .subscribe((boards: IBoard[]) => (this.boardsCollection = boards));
  }

  private getQueryFromForm(): { [filter: string]: number | string } {
    const options: { [filter: string]: number | string } = {};
    if (this.searchForm.get('board')?.value) {
      options['boardId.equals'] = this.searchForm.get('board')?.value.id;
    }
    if (this.searchForm.get('type')?.value) {
      options['type.equals'] = this.searchForm.get('type')?.value;
    }
    return options;
  }
}
