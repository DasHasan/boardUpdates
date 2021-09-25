import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISoftware, Software } from '../software.model';
import { SoftwareService } from '../service/software.service';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';

@Component({
  selector: 'jhi-software-update',
  templateUrl: './software-update.component.html',
})
export class SoftwareUpdateComponent implements OnInit {
  isSaving = false;

  boardsSharedCollection: IBoard[] = [];

  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
    board: [],
  });

  constructor(
    protected softwareService: SoftwareService,
    protected boardService: BoardService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ software }) => {
      this.updateForm(software);

      this.loadRelationshipsOptions();
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

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
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
      board: software.board,
    });

    this.boardsSharedCollection = this.boardService.addBoardToCollectionIfMissing(this.boardsSharedCollection, software.board);
  }

  protected loadRelationshipsOptions(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .pipe(map((boards: IBoard[]) => this.boardService.addBoardToCollectionIfMissing(boards, this.editForm.get('board')!.value)))
      .subscribe((boards: IBoard[]) => (this.boardsSharedCollection = boards));
  }

  protected createFromForm(): ISoftware {
    return {
      ...new Software(),
      id: this.editForm.get(['id'])!.value,
      version: this.editForm.get(['version'])!.value,
      path: this.editForm.get(['path'])!.value,
      board: this.editForm.get(['board'])!.value,
    };
  }
}
