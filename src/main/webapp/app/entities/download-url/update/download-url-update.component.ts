import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDownloadUrl, DownloadUrl } from '../download-url.model';
import { DownloadUrlService } from '../service/download-url.service';

@Component({
  selector: 'jhi-download-url-update',
  templateUrl: './download-url-update.component.html',
})
export class DownloadUrlUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    expirationDate: [],
    url: [],
  });

  constructor(protected downloadUrlService: DownloadUrlService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ downloadUrl }) => {
      this.updateForm(downloadUrl);
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
    });
  }

  protected createFromForm(): IDownloadUrl {
    return {
      ...new DownloadUrl(),
      id: this.editForm.get(['id'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      url: this.editForm.get(['url'])!.value,
    };
  }
}
