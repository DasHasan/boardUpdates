import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFirmware, Firmware } from '../firmware.model';
import { FirmwareService } from '../service/firmware.service';

@Component({
  selector: 'jhi-firmware-update',
  templateUrl: './firmware-update.component.html',
})
export class FirmwareUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
  });

  constructor(protected firmwareService: FirmwareService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ firmware }) => {
      this.updateForm(firmware);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const firmware = this.createFromForm();
    if (firmware.id !== undefined) {
      this.subscribeToSaveResponse(this.firmwareService.update(firmware));
    } else {
      this.subscribeToSaveResponse(this.firmwareService.create(firmware));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFirmware>>): void {
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

  protected updateForm(firmware: IFirmware): void {
    this.editForm.patchValue({
      id: firmware.id,
      version: firmware.version,
      path: firmware.path,
    });
  }

  protected createFromForm(): IFirmware {
    return {
      ...new Firmware(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
      path: this.editForm.get(['path'])!.value,
    };
  }
}
