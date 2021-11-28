import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBoardUpdateSuccessor, BoardUpdateSuccessor } from '../board-update-successor.model';
import { BoardUpdateSuccessorService } from '../service/board-update-successor.service';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';

@Component({
  selector: 'jhi-board-update-successor-update',
  styles: [
    `
      .hidden {
        display: none;
      }
    `,
  ],
  templateUrl: './board-update-successor-update.component.html',
})
export class BoardUpdateSuccessorUpdateComponent implements OnInit {
  isSaving = false;

  boardUpdateSuccessor?: BoardUpdateSuccessor;
  fromsCollection: IBoardUpdate[] = [];
  tosCollection: IBoardUpdate[] = [];

  editForm = this.fb.group({
    id: [],
    from: [],
    to: [],
  });

  constructor(
    protected boardUpdateSuccessorService: BoardUpdateSuccessorService,
    protected boardUpdateService: BoardUpdateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUpdateSuccessor }) => {
      this.boardUpdateSuccessor = boardUpdateSuccessor;
      this.updateForm(boardUpdateSuccessor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const boardUpdateSuccessor = this.createFromForm();
    if (boardUpdateSuccessor.id !== undefined) {
      this.subscribeToSaveResponse(this.boardUpdateSuccessorService.update(boardUpdateSuccessor));
    } else {
      this.subscribeToSaveResponse(this.boardUpdateSuccessorService.create(boardUpdateSuccessor));
    }
  }

  trackBoardUpdateById(index: number, item: IBoardUpdate): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoardUpdateSuccessor>>): void {
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

  protected updateForm(boardUpdateSuccessor: IBoardUpdateSuccessor): void {
    this.editForm.patchValue({
      id: boardUpdateSuccessor.id,
      from: boardUpdateSuccessor.from,
      to: boardUpdateSuccessor.to,
    });

    this.fromsCollection = this.boardUpdateService.addBoardUpdateToCollectionIfMissing(this.fromsCollection, boardUpdateSuccessor.from);
    this.tosCollection = this.boardUpdateService.addBoardUpdateToCollectionIfMissing(this.tosCollection, boardUpdateSuccessor.to);
  }

  protected loadRelationshipsOptions(): void {
    this.boardUpdateService
      .query({ 'boardUpdateSuccessorId.specified': 'false' })
      .pipe(map((res: HttpResponse<IBoardUpdate[]>) => res.body ?? []))
      .pipe(
        map((boardUpdates: IBoardUpdate[]) =>
          this.boardUpdateService.addBoardUpdateToCollectionIfMissing(boardUpdates, this.editForm.get('from')!.value)
        )
      )
      .subscribe((boardUpdates: IBoardUpdate[]) => (this.fromsCollection = boardUpdates));

    this.boardUpdateService
      .query({ 'boardUpdateSuccessorId.specified': 'false' })
      .pipe(map((res: HttpResponse<IBoardUpdate[]>) => res.body ?? []))
      .pipe(
        map((boardUpdates: IBoardUpdate[]) =>
          this.boardUpdateService.addBoardUpdateToCollectionIfMissing(boardUpdates, this.editForm.get('to')!.value)
        )
      )
      .subscribe((boardUpdates: IBoardUpdate[]) => (this.tosCollection = boardUpdates));
  }

  protected createFromForm(): IBoardUpdateSuccessor {
    return {
      ...new BoardUpdateSuccessor(),
      id: this.editForm.get(['id'])!.value,
      from: this.editForm.get(['from'])!.value,
      to: this.editForm.get(['to'])!.value,
    };
  }
}
