import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFirmware, Firmware } from '../firmware.model';
import { FirmwareService } from '../service/firmware.service';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';

@Component({
  selector: 'jhi-firmware-update',
  templateUrl: './firmware-update.component.html',
})
export class FirmwareUpdateComponent implements OnInit {
  isSaving = false;

  boardsSharedCollection: IBoard[] = [];

  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
    board: [],
  });

  constructor(
    protected firmwareService: FirmwareService,
    protected boardService: BoardService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ firmware }) => {
      this.updateForm(firmware);

      this.loadRelationshipsOptions();
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

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
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
      board: firmware.board,
    });

    this.boardsSharedCollection = this.boardService.addBoardToCollectionIfMissing(this.boardsSharedCollection, firmware.board);
  }

  protected loadRelationshipsOptions(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .pipe(map((boards: IBoard[]) => this.boardService.addBoardToCollectionIfMissing(boards, this.editForm.get('board')!.value)))
      .subscribe((boards: IBoard[]) => (this.boardsSharedCollection = boards));
  }

  protected createFromForm(): IFirmware {
    return {
      ...new Firmware(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
      path: this.editForm.get(['path'])!.value,
      board: this.editForm.get(['board'])!.value,
    };
  }
}
