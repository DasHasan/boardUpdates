import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDownloadUrl, DownloadUrl } from '../download-url.model';
import { DownloadUrlService } from '../service/download-url.service';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

@Component({
  selector: 'jhi-download-url-update',
  templateUrl: './download-url-update.component.html',
})
export class DownloadUrlUpdateComponent implements OnInit {
  isSaving = false;

  boardUpdatesCollection: IBoardUpdate[] = [];

  editForm = this.fb.group({
    id: [],
    expirationDate: [],
    url: [],
    boardUpdate: [],
  });

  constructor(
    protected downloadUrlService: DownloadUrlService,
    protected boardUpdateService: BoardUpdateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ downloadUrl }) => {
      this.updateForm(downloadUrl);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const downloadUrl = this.createFromForm();
    if (downloadUrl.id !== undefined) {
      this.subscribeToSaveResponse(this.downloadUrlService.update(downloadUrl));
    } else {
      this.subscribeToSaveResponse(this.downloadUrlService.create(downloadUrl));
    }
  }

  trackBoardUpdateById(index: number, item: IBoardUpdate): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDownloadUrl>>): void {
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

  protected updateForm(downloadUrl: IDownloadUrl): void {
    this.editForm.patchValue({
      id: downloadUrl.id,
      expirationDate: downloadUrl.expirationDate,
      url: downloadUrl.url,
      boardUpdate: downloadUrl.boardUpdate,
    });

    this.boardUpdatesCollection = this.boardUpdateService.addBoardUpdateToCollectionIfMissing(
      this.boardUpdatesCollection,
      downloadUrl.boardUpdate
    );
  }

  protected loadRelationshipsOptions(): void {
    this.boardUpdateService
      .query({ 'downloadUrlId.specified': 'false' })
      .pipe(map((res: HttpResponse<IBoardUpdate[]>) => res.body ?? []))
      .pipe(
        map((boardUpdates: IBoardUpdate[]) =>
          this.boardUpdateService.addBoardUpdateToCollectionIfMissing(boardUpdates, this.editForm.get('boardUpdate')!.value)
        )
      )
      .subscribe((boardUpdates: IBoardUpdate[]) => (this.boardUpdatesCollection = boardUpdates));
  }

  protected createFromForm(): IDownloadUrl {
    return {
      ...new DownloadUrl(),
      id: this.editForm.get(['id'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      url: this.editForm.get(['url'])!.value,
      boardUpdate: this.editForm.get(['boardUpdate'])!.value,
    };
  }
}
