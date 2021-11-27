import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDownload, Download } from '../download.model';
import { DownloadService } from '../service/download.service';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

@Component({
  selector: 'jhi-download-update',
  templateUrl: './download-update.component.html',
})
export class DownloadUpdateComponent implements OnInit {
  isSaving = false;

  boardUpdatesCollection: IBoardUpdate[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    boardUpdate: [],
  });

  constructor(
    protected downloadService: DownloadService,
    protected boardUpdateService: BoardUpdateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ download }) => {
      if (download.id === undefined) {
        const today = dayjs().startOf('day');
        download.date = today;
      }

      this.updateForm(download);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const download = this.createFromForm();
    if (download.id !== undefined) {
      this.subscribeToSaveResponse(this.downloadService.update(download));
    } else {
      this.subscribeToSaveResponse(this.downloadService.create(download));
    }
  }

  trackBoardUpdateById(index: number, item: IBoardUpdate): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDownload>>): void {
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

  protected updateForm(download: IDownload): void {
    this.editForm.patchValue({
      id: download.id,
      date: download.date ? download.date.format(DATE_TIME_FORMAT) : null,
      boardUpdate: download.boardUpdate,
    });

    this.boardUpdatesCollection = this.boardUpdateService.addBoardUpdateToCollectionIfMissing(
      this.boardUpdatesCollection,
      download.boardUpdate
    );
  }

  protected loadRelationshipsOptions(): void {
    this.boardUpdateService
      .query({ 'downloadId.specified': 'false' })
      .pipe(map((res: HttpResponse<IBoardUpdate[]>) => res.body ?? []))
      .pipe(
        map((boardUpdates: IBoardUpdate[]) =>
          this.boardUpdateService.addBoardUpdateToCollectionIfMissing(boardUpdates, this.editForm.get('boardUpdate')!.value)
        )
      )
      .subscribe((boardUpdates: IBoardUpdate[]) => (this.boardUpdatesCollection = boardUpdates));
  }

  protected createFromForm(): IDownload {
    return {
      ...new Download(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      boardUpdate: this.editForm.get(['boardUpdate'])!.value,
    };
  }
}
