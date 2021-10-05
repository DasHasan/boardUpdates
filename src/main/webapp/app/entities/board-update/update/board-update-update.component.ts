import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBoardUpdate, BoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';

@Component({
  selector: 'jhi-board-update-update',
  templateUrl: './board-update-update.component.html',
})
export class BoardUpdateUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
    type: [],
    releaseDate: [],
  });

  constructor(protected boardUpdateService: BoardUpdateService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUpdate }) => {
      if (boardUpdate.id === undefined) {
        const today = dayjs().startOf('day');
        boardUpdate.releaseDate = today;
      }

      this.updateForm(boardUpdate);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const boardUpdate = this.createFromForm();
    if (boardUpdate.id !== undefined) {
      this.subscribeToSaveResponse(this.boardUpdateService.update(boardUpdate));
    } else {
      this.subscribeToSaveResponse(this.boardUpdateService.create(boardUpdate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoardUpdate>>): void {
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

  protected updateForm(boardUpdate: IBoardUpdate): void {
    this.editForm.patchValue({
      id: boardUpdate.id,
      version: boardUpdate.version,
      path: boardUpdate.path,
      type: boardUpdate.type,
      releaseDate: boardUpdate.releaseDate ? boardUpdate.releaseDate.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IBoardUpdate {
    return {
      ...new BoardUpdate(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
      path: this.editForm.get(['path'])!.value,
      type: this.editForm.get(['type'])!.value,
      releaseDate: this.editForm.get(['releaseDate'])!.value
        ? dayjs(this.editForm.get(['releaseDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
    };
  }
}
