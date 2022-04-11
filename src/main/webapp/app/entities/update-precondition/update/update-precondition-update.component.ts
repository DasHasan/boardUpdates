import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUpdatePrecondition, UpdatePrecondition } from '../update-precondition.model';
import { UpdatePreconditionService } from '../service/update-precondition.service';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

@Component({
  selector: 'jhi-update-precondition-update',
  templateUrl: './update-precondition-update.component.html',
})
export class UpdatePreconditionUpdateComponent implements OnInit {
  isSaving = false;

  boardUpdatesSharedCollection: IBoardUpdate[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    boardUpdate: [],
  });

  constructor(
    protected updatePreconditionService: UpdatePreconditionService,
    protected boardUpdateService: BoardUpdateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ updatePrecondition }) => {
      this.updateForm(updatePrecondition);

      this.loadRelationshipsOptions();
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

  trackBoardUpdateById(index: number, item: IBoardUpdate): number {
    return item.id!;
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
      status: updatePrecondition.status,
      boardUpdate: updatePrecondition.boardUpdate,
    });

    this.boardUpdatesSharedCollection = this.boardUpdateService.addBoardUpdateToCollectionIfMissing(
      this.boardUpdatesSharedCollection,
      updatePrecondition.boardUpdate
    );
  }

  protected loadRelationshipsOptions(): void {
    this.boardUpdateService
      .query()
      .pipe(map((res: HttpResponse<IBoardUpdate[]>) => res.body ?? []))
      .pipe(
        map((boardUpdates: IBoardUpdate[]) =>
          this.boardUpdateService.addBoardUpdateToCollectionIfMissing(boardUpdates, this.editForm.get('boardUpdate')!.value)
        )
      )
      .subscribe((boardUpdates: IBoardUpdate[]) => (this.boardUpdatesSharedCollection = boardUpdates));
  }

  protected createFromForm(): IUpdatePrecondition {
    return {
      ...new UpdatePrecondition(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      boardUpdate: this.editForm.get(['boardUpdate'])!.value,
    };
  }
}
