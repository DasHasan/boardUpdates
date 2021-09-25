import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFirmwareUpdate, FirmwareUpdate } from '../firmware-update.model';
import { FirmwareUpdateService } from '../service/firmware-update.service';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { ISoftware } from 'app/entities/software/software.model';
import { SoftwareService } from 'app/entities/software/service/software.service';

@Component({
  selector: 'jhi-firmware-update-update',
  templateUrl: './firmware-update-update.component.html',
})
export class FirmwareUpdateUpdateComponent implements OnInit {
  isSaving = false;

  boardsSharedCollection: IBoard[] = [];
  softwareSharedCollection: ISoftware[] = [];

  editForm = this.fb.group({
    id: [],
    active: [],
    board: [],
    from: [],
    to: [],
  });

  constructor(
    protected firmwareUpdateService: FirmwareUpdateService,
    protected boardService: BoardService,
    protected softwareService: SoftwareService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ firmwareUpdate }) => {
      this.updateForm(firmwareUpdate);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const firmwareUpdate = this.createFromForm();
    if (firmwareUpdate.id !== undefined) {
      this.subscribeToSaveResponse(this.firmwareUpdateService.update(firmwareUpdate));
    } else {
      this.subscribeToSaveResponse(this.firmwareUpdateService.create(firmwareUpdate));
    }
  }

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
  }

  trackSoftwareById(index: number, item: ISoftware): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFirmwareUpdate>>): void {
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

  protected updateForm(firmwareUpdate: IFirmwareUpdate): void {
    this.editForm.patchValue({
      id: firmwareUpdate.id,
      active: firmwareUpdate.active,
      board: firmwareUpdate.board,
      from: firmwareUpdate.from,
      to: firmwareUpdate.to,
    });

    this.boardsSharedCollection = this.boardService.addBoardToCollectionIfMissing(this.boardsSharedCollection, firmwareUpdate.board);
    this.softwareSharedCollection = this.softwareService.addSoftwareToCollectionIfMissing(
      this.softwareSharedCollection,
      firmwareUpdate.from,
      firmwareUpdate.to
    );
  }

  protected loadRelationshipsOptions(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .pipe(map((boards: IBoard[]) => this.boardService.addBoardToCollectionIfMissing(boards, this.editForm.get('board')!.value)))
      .subscribe((boards: IBoard[]) => (this.boardsSharedCollection = boards));

    this.softwareService
      .query()
      .pipe(map((res: HttpResponse<ISoftware[]>) => res.body ?? []))
      .pipe(
        map((software: ISoftware[]) =>
          this.softwareService.addSoftwareToCollectionIfMissing(software, this.editForm.get('from')!.value, this.editForm.get('to')!.value)
        )
      )
      .subscribe((software: ISoftware[]) => (this.softwareSharedCollection = software));
  }

  protected createFromForm(): IFirmwareUpdate {
    return {
      ...new FirmwareUpdate(),
      id: this.editForm.get(['id'])!.value,
      active: this.editForm.get(['active'])!.value,
      board: this.editForm.get(['board'])!.value,
      from: this.editForm.get(['from'])!.value,
      to: this.editForm.get(['to'])!.value,
    };
  }
}
