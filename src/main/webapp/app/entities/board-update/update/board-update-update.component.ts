import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map, switchMap } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBoardUpdate, BoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { FileService } from 'app/shared/file.service';

@Component({
  selector: 'jhi-board-update-update',
  styles: [
    `
      .hidden {
        display: none;
      }
    `,
  ],
  templateUrl: './board-update-update.component.html',
})
export class BoardUpdateUpdateComponent implements OnInit {
  isSaving = false;

  boardsSharedCollection: IBoard[] = [];

  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
    type: [],
    releaseDate: [],
    status: [],
    board: [],
  });
  private file: File | undefined;

  constructor(
    protected boardUpdateService: BoardUpdateService,
    protected boardService: BoardService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected fileService: FileService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUpdate }) => {
      if (boardUpdate.id === undefined) {
        const today = dayjs().startOf('day');
        boardUpdate.releaseDate = today;
      }

      this.updateForm(boardUpdate);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const boardUpdate = this.createFromForm();
    if (boardUpdate.id !== undefined) {
      this.subscribeToSaveResponse(this.boardUpdateService.update(boardUpdate));
    } else {
      this.subscribeToSaveResponse(this.boardUpdateService.create(boardUpdate));
    }
  }

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
  }

  fileChanged($event: Event): void {
    const target = $event.target as HTMLInputElement;
    if (target.files) {
      const file = target.files[0];
      this.file = file;
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoardUpdate>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      ({ body: update }) => this.onSaveSuccess(update),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(update: IBoardUpdate | null): void {
    if (this.file) {
      if (update?.board?.serial && update.type && update.version) {
        this.fileService
          .upload(this.file, `${update.board.serial}/${update.type}`, `autoupdate_${update.version}.zip`)
          .pipe(switchMap(({ path }) => this.boardUpdateService.update({ ...update, path })))
          .subscribe();
      }
    }
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(boardUpdate: IBoardUpdate): void {
    this.editForm.patchValue({
      id: boardUpdate.id,
      version: boardUpdate.version,
      path: boardUpdate.path,
      type: boardUpdate.type,
      releaseDate: boardUpdate.releaseDate ? boardUpdate.releaseDate.format(DATE_TIME_FORMAT) : null,
      status: boardUpdate.status,
      board: boardUpdate.board,
    });

    this.boardsSharedCollection = this.boardService.addBoardToCollectionIfMissing(this.boardsSharedCollection, boardUpdate.board);
  }

  protected loadRelationshipsOptions(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .pipe(map((boards: IBoard[]) => this.boardService.addBoardToCollectionIfMissing(boards, this.editForm.get('board')!.value)))
      .subscribe((boards: IBoard[]) => (this.boardsSharedCollection = boards));
  }

  protected createFromForm(): IBoardUpdate {
    return {
      ...new BoardUpdate(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
      path: this.editForm.get(['path'])!.value,
      type: this.editForm.get(['type'])!.value,
      releaseDate: this.editForm.get(['releaseDate'])!.value
        ? dayjs(this.editForm.get(['releaseDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      status: this.editForm.get(['status'])!.value,
      board: this.editForm.get(['board'])!.value,
    };
  }
}
