import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBoardUpdate, BoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';
import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';
import { UpdatePreconditionService } from 'app/entities/update-precondition/service/update-precondition.service';
import { IDownloadUrl } from 'app/entities/download-url/download-url.model';
import { DownloadUrlService } from 'app/entities/download-url/service/download-url.service';

@Component({
  selector: 'jhi-board-update-update',
  templateUrl: './board-update-update.component.html',
})
export class BoardUpdateUpdateComponent implements OnInit {
  isSaving = false;

  updatePreconditionsCollection: IUpdatePrecondition[] = [];
  downloadUrlsCollection: IDownloadUrl[] = [];

  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
    type: [],
    releaseDate: [],
    status: [],
    updatePrecondition: [],
    downloadUrl: [],
  });

  constructor(
    protected boardUpdateService: BoardUpdateService,
    protected updatePreconditionService: UpdatePreconditionService,
    protected downloadUrlService: DownloadUrlService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
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

  trackUpdatePreconditionById(index: number, item: IUpdatePrecondition): number {
    return item.id!;
  }

  trackDownloadUrlById(index: number, item: IDownloadUrl): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoardUpdate>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
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
      updatePrecondition: boardUpdate.updatePrecondition,
      downloadUrl: boardUpdate.downloadUrl,
    });

    this.updatePreconditionsCollection = this.updatePreconditionService.addUpdatePreconditionToCollectionIfMissing(
      this.updatePreconditionsCollection,
      boardUpdate.updatePrecondition
    );
    this.downloadUrlsCollection = this.downloadUrlService.addDownloadUrlToCollectionIfMissing(
      this.downloadUrlsCollection,
      boardUpdate.downloadUrl
    );
  }

  protected loadRelationshipsOptions(): void {
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

    this.downloadUrlService
      .query({ 'boardUpdateId.specified': 'false' })
      .pipe(map((res: HttpResponse<IDownloadUrl[]>) => res.body ?? []))
      .pipe(
        map((downloadUrls: IDownloadUrl[]) =>
          this.downloadUrlService.addDownloadUrlToCollectionIfMissing(downloadUrls, this.editForm.get('downloadUrl')!.value)
        )
      )
      .subscribe((downloadUrls: IDownloadUrl[]) => (this.downloadUrlsCollection = downloadUrls));
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
      updatePrecondition: this.editForm.get(['updatePrecondition'])!.value,
      downloadUrl: this.editForm.get(['downloadUrl'])!.value,
    };
  }
}
