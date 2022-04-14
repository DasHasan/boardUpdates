import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUpdateVersionPrecondition, UpdateVersionPrecondition } from '../update-version-precondition.model';
import { UpdateVersionPreconditionService } from '../service/update-version-precondition.service';

@Component({
  selector: 'jhi-update-version-precondition-update',
  templateUrl: './update-version-precondition-update.component.html',
})
export class UpdateVersionPreconditionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    version: [],
  });

  constructor(
    protected updateVersionPreconditionService: UpdateVersionPreconditionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ updateVersionPrecondition }) => {
      this.updateForm(updateVersionPrecondition);
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
    });
  }

  protected createFromForm(): IUpdateVersionPrecondition {
    return {
      ...new UpdateVersionPrecondition(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
    };
  }
}
