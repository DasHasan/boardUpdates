import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { iif, Observable, of } from 'rxjs';
import { finalize, map, switchMap } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { BoardUpdate, IBoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';
import { UpdatePreconditionService } from 'app/entities/update-precondition/service/update-precondition.service';
import { FileService } from 'app/shared/file.service';
import { DownloadUrlService } from 'app/entities/download-url/service/download-url.service';
import { DownloadUrl, IDownloadUrl } from 'app/entities/download-url/download-url.model';

@Component({
  selector: 'jhi-board-update-update',
  templateUrl: './board-update-update.component.html',
})
export class BoardUpdateUpdateComponent implements OnInit {
  isSaving = false;

  boardsSharedCollection: IBoard[] = [];
  updatePreconditionsCollection: IUpdatePrecondition[] = [];

  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
    type: [],
    releaseDate: [],
    status: [],
    board: [],
    updatePrecondition: [],
  });
  boardUpdate?: BoardUpdate;
  private file: File | undefined;

  constructor(
    protected boardUpdateService: BoardUpdateService,
    protected downloadUrlService: DownloadUrlService,
    protected boardService: BoardService,
    protected updatePreconditionService: UpdatePreconditionService,
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
      this.boardUpdate = boardUpdate;

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

  trackUpdatePreconditionById(index: number, item: IUpdatePrecondition): number {
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
    result
      .pipe(
        switchMap(boardUpdate => this.uploadFile(boardUpdate)),
        switchMap(boardUpdate => this.createDownloadUrlIfMissing(boardUpdate)),
        finalize(() => this.onSaveFinalize())
      )
      .subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
  }

  protected onSaveSuccess(): void {
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
      updatePrecondition: boardUpdate.updatePrecondition,
    });

    this.boardsSharedCollection = this.boardService.addBoardToCollectionIfMissing(this.boardsSharedCollection, boardUpdate.board);
    this.updatePreconditionsCollection = this.updatePreconditionService.addUpdatePreconditionToCollectionIfMissing(
      this.updatePreconditionsCollection,
      boardUpdate.updatePrecondition
    );
  }

  protected loadRelationshipsOptions(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .pipe(map((boards: IBoard[]) => this.boardService.addBoardToCollectionIfMissing(boards, this.editForm.get('board')!.value)))
      .subscribe((boards: IBoard[]) => (this.boardsSharedCollection = boards));

    this.updatePreconditionService
      .query({ filter: 'update-is-null' })
      .pipe(map((res: HttpResponse<IUpdatePrecondition[]>) => res.body ?? []))
      .pipe(
        map((updatePreconditions: IUpdatePrecondition[]) =>
          this.updatePreconditionService.addUpdatePreconditionToCollectionIfMissing(
            updatePreconditions,
            this.editForm.get('updatePrecondition')!.value
          )
        )
      )
      .subscribe((updatePreconditions: IUpdatePrecondition[]) => (this.updatePreconditionsCollection = updatePreconditions));
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
      updatePrecondition: this.editForm.get(['updatePrecondition'])!.value,
    };
  }

  private uploadFile(response: HttpResponse<IBoardUpdate>): Observable<HttpResponse<IBoardUpdate>> {
    const update = response.body;

    if (update?.board?.serial && update.type && update.version && this.file) {
      return this.fileService
        .upload(this.file, `${update.board.serial}/${update.type}`, this.file.name)
        .pipe(switchMap(({ path }) => this.boardUpdateService.update({ ...update, path })));
    }

    return of(response);
  }

  private createDownloadUrlIfMissing(response: HttpResponse<IBoardUpdate>): Observable<HttpResponse<IBoardUpdate>> {
    if (!response.body) {
      return of(response);
    }

    if (this.boardUpdate?.id) {
      return of(response);
    }

    const observable = this.downloadUrlService
      .create(new DownloadUrl(undefined, dayjs().startOf('day').add(1, 'day'), undefined, response.body))
      .pipe(map(() => response));
    this.downloadUrlService
      .query({ 'boardUpdateId.equals': response.body.id })
      .pipe(switchMap((value: HttpResponse<IDownloadUrl[]>) => iif(() => value.body!.length > 0, of(response), observable)));
    return observable;
  }
}
