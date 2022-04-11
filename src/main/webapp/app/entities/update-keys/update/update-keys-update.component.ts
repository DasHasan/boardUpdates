import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUpdateKeys, UpdateKeys } from '../update-keys.model';
import { UpdateKeysService } from '../service/update-keys.service';
import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';
import { UpdatePreconditionService } from 'app/entities/update-precondition/service/update-precondition.service';

@Component({
  selector: 'jhi-update-keys-update',
  templateUrl: './update-keys-update.component.html',
})
export class UpdateKeysUpdateComponent implements OnInit {
  isSaving = false;

  updatePreconditionsSharedCollection: IUpdatePrecondition[] = [];

  editForm = this.fb.group({
    id: [],
    key: [],
    updatePrecondition: [],
  });

  constructor(
    protected updateKeysService: UpdateKeysService,
    protected updatePreconditionService: UpdatePreconditionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ updateKeys }) => {
      this.updateForm(updateKeys);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const updateKeys = this.createFromForm();
    if (updateKeys.id !== undefined) {
      this.subscribeToSaveResponse(this.updateKeysService.update(updateKeys));
    } else {
      this.subscribeToSaveResponse(this.updateKeysService.create(updateKeys));
    }
  }

  trackUpdatePreconditionById(index: number, item: IUpdatePrecondition): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUpdateKeys>>): void {
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

  protected updateForm(updateKeys: IUpdateKeys): void {
    this.editForm.patchValue({
      id: updateKeys.id,
      key: updateKeys.key,
      updatePrecondition: updateKeys.updatePrecondition,
    });

    this.updatePreconditionsSharedCollection = this.updatePreconditionService.addUpdatePreconditionToCollectionIfMissing(
      this.updatePreconditionsSharedCollection,
      updateKeys.updatePrecondition
    );
  }

  protected loadRelationshipsOptions(): void {
    this.updatePreconditionService
      .query()
      .pipe(map((res: HttpResponse<IUpdatePrecondition[]>) => res.body ?? []))
      .pipe(
        map((updatePreconditions: IUpdatePrecondition[]) =>
          this.updatePreconditionService.addUpdatePreconditionToCollectionIfMissing(
            updatePreconditions,
            this.editForm.get('updatePrecondition')!.value
          )
        )
      )
      .subscribe((updatePreconditions: IUpdatePrecondition[]) => (this.updatePreconditionsSharedCollection = updatePreconditions));
  }

  protected createFromForm(): IUpdateKeys {
    return {
      ...new UpdateKeys(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      updatePrecondition: this.editForm.get(['updatePrecondition'])!.value,
    };
  }
}
