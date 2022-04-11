import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBoard, Board } from '../board.model';
import { BoardService } from '../service/board.service';
import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';
import { UpdatePreconditionService } from 'app/entities/update-precondition/service/update-precondition.service';

@Component({
  selector: 'jhi-board-update',
  templateUrl: './board-update.component.html',
})
export class BoardUpdateComponent implements OnInit {
  isSaving = false;

  updatePreconditionsSharedCollection: IUpdatePrecondition[] = [];

  editForm = this.fb.group({
    id: [],
    serial: [],
    version: [],
    updatePrecondition: [],
  });

  constructor(
    protected boardService: BoardService,
    protected updatePreconditionService: UpdatePreconditionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ board }) => {
      this.updateForm(board);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const board = this.createFromForm();
    if (board.id !== undefined) {
      this.subscribeToSaveResponse(this.boardService.update(board));
    } else {
      this.subscribeToSaveResponse(this.boardService.create(board));
    }
  }

  trackUpdatePreconditionById(index: number, item: IUpdatePrecondition): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoard>>): void {
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

  protected updateForm(board: IBoard): void {
    this.editForm.patchValue({
      id: board.id,
      serial: board.serial,
      version: board.version,
      updatePrecondition: board.updatePrecondition,
    });

    this.updatePreconditionsSharedCollection = this.updatePreconditionService.addUpdatePreconditionToCollectionIfMissing(
      this.updatePreconditionsSharedCollection,
      board.updatePrecondition
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

  protected createFromForm(): IBoard {
    return {
      ...new Board(),
      id: this.editForm.get(['id'])!.value,
      serial: this.editForm.get(['serial'])!.value,
      version: this.editForm.get(['version'])!.value,
      updatePrecondition: this.editForm.get(['updatePrecondition'])!.value,
    };
  }
}
