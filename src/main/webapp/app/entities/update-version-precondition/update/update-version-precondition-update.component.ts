import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUpdateVersionPrecondition, UpdateVersionPrecondition } from '../update-version-precondition.model';
import { UpdateVersionPreconditionService } from '../service/update-version-precondition.service';
import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';
import { UpdatePreconditionService } from 'app/entities/update-precondition/service/update-precondition.service';

@Component({
  selector: 'jhi-update-version-precondition-update',
  templateUrl: './update-version-precondition-update.component.html',
})
export class UpdateVersionPreconditionUpdateComponent implements OnInit {
  isSaving = false;

  updatePreconditionsSharedCollection: IUpdatePrecondition[] = [];

  editForm = this.fb.group({
    id: [],
    version: [],
    type: [],
    updatePrecondition: [],
  });

  constructor(
    protected updateVersionPreconditionService: UpdateVersionPreconditionService,
    protected updatePreconditionService: UpdatePreconditionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ updateVersionPrecondition }) => {
      this.updateForm(updateVersionPrecondition);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const updateVersionPrecondition = this.createFromForm();
    if (updateVersionPrecondition.id !== undefined) {
      this.subscribeToSaveResponse(this.updateVersionPreconditionService.update(updateVersionPrecondition));
    } else {
      this.subscribeToSaveResponse(this.updateVersionPreconditionService.create(updateVersionPrecondition));
    }
  }

  trackUpdatePreconditionById(index: number, item: IUpdatePrecondition): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUpdateVersionPrecondition>>): void {
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

  protected updateForm(updateVersionPrecondition: IUpdateVersionPrecondition): void {
    this.editForm.patchValue({
      id: updateVersionPrecondition.id,
      version: updateVersionPrecondition.version,
      type: updateVersionPrecondition.type,
      updatePrecondition: updateVersionPrecondition.updatePrecondition,
    });

    this.updatePreconditionsSharedCollection = this.updatePreconditionService.addUpdatePreconditionToCollectionIfMissing(
      this.updatePreconditionsSharedCollection,
      updateVersionPrecondition.updatePrecondition
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

  protected createFromForm(): IUpdateVersionPrecondition {
    return {
      ...new UpdateVersionPrecondition(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
      type: this.editForm.get(['type'])!.value,
      updatePrecondition: this.editForm.get(['updatePrecondition'])!.value,
    };
  }
}
