import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISoftware, Software } from '../software.model';
import { SoftwareService } from '../service/software.service';

@Component({
  selector: 'jhi-software-update',
  templateUrl: './software-update.component.html',
})
export class SoftwareUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
  });

  constructor(protected softwareService: SoftwareService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ software }) => {
      this.updateForm(software);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const software = this.createFromForm();
    if (software.id !== undefined) {
      this.subscribeToSaveResponse(this.softwareService.update(software));
    } else {
      this.subscribeToSaveResponse(this.softwareService.create(software));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoftware>>): void {
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

  protected updateForm(software: ISoftware): void {
    this.editForm.patchValue({
      id: software.id,
      version: software.version,
      path: software.path,
    });
  }

  protected createFromForm(): ISoftware {
    return {
      ...new Software(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
      path: this.editForm.get(['path'])!.value,
    };
  }
}
