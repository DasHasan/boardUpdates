import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISoftwareUpdate, SoftwareUpdate } from '../software-update.model';
import { SoftwareUpdateService } from '../service/software-update.service';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { ISoftware } from 'app/entities/software/software.model';
import { SoftwareService } from 'app/entities/software/service/software.service';

@Component({
  selector: 'jhi-software-update-update',
  templateUrl: './software-update-update.component.html',
})
export class SoftwareUpdateUpdateComponent implements OnInit {
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
    protected softwareUpdateService: SoftwareUpdateService,
    protected boardService: BoardService,
    protected softwareService: SoftwareService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ softwareUpdate }) => {
      this.updateForm(softwareUpdate);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const softwareUpdate = this.createFromForm();
    if (softwareUpdate.id !== undefined) {
      this.subscribeToSaveResponse(this.softwareUpdateService.update(softwareUpdate));
    } else {
      this.subscribeToSaveResponse(this.softwareUpdateService.create(softwareUpdate));
    }
  }

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
  }

  trackSoftwareById(index: number, item: ISoftware): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoftwareUpdate>>): void {
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

  protected updateForm(softwareUpdate: ISoftwareUpdate): void {
    this.editForm.patchValue({
      id: softwareUpdate.id,
      active: softwareUpdate.active,
      board: softwareUpdate.board,
      from: softwareUpdate.from,
      to: softwareUpdate.to,
    });

    this.boardsSharedCollection = this.boardService.addBoardToCollectionIfMissing(this.boardsSharedCollection, softwareUpdate.board);
    this.softwareSharedCollection = this.softwareService.addSoftwareToCollectionIfMissing(
      this.softwareSharedCollection,
      softwareUpdate.from,
      softwareUpdate.to
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

  protected createFromForm(): ISoftwareUpdate {
    return {
      ...new SoftwareUpdate(),
      id: this.editForm.get(['id'])!.value,
      active: this.editForm.get(['active'])!.value,
      board: this.editForm.get(['board'])!.value,
      from: this.editForm.get(['from'])!.value,
      to: this.editForm.get(['to'])!.value,
    };
  }
}
