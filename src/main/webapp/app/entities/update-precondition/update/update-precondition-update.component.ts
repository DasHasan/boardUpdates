import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUpdatePrecondition, UpdatePrecondition } from '../update-precondition.model';
import { UpdatePreconditionService } from '../service/update-precondition.service';

@Component({
  selector: 'jhi-update-precondition-update',
  templateUrl: './update-precondition-update.component.html',
})
export class UpdatePreconditionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(
    protected updatePreconditionService: UpdatePreconditionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ updatePrecondition }) => {
      this.updateForm(updatePrecondition);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const updatePrecondition = this.createFromForm();
    if (updatePrecondition.id !== undefined) {
      this.subscribeToSaveResponse(this.updatePreconditionService.update(updatePrecondition));
    } else {
      this.subscribeToSaveResponse(this.updatePreconditionService.create(updatePrecondition));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUpdatePrecondition>>): void {
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

  protected updateForm(updatePrecondition: IUpdatePrecondition): void {
    this.editForm.patchValue({
      id: updatePrecondition.id,
    });
  }

  protected createFromForm(): IUpdatePrecondition {
    return {
      ...new UpdatePrecondition(),
      id: this.editForm.get(['id'])!.value,
    };
  }
}
