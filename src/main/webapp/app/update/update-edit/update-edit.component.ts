import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { IBoard } from 'app/entities/board/board.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { finalize, map, switchMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';
import { ActivatedRoute } from '@angular/router';
import * as dayjs from 'dayjs';
import { BoardUpdate, IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { UpdateService } from 'app/update/update.service';

@Component({
  selector: 'jhi-update-edit',
  templateUrl: './update-edit.component.html',
  styleUrls: ['./update-edit.component.scss'],
})
export class UpdateEditComponent implements OnInit {
  @Input() id?: number;
  isSaving = false;

  boardsSharedCollection: IBoard[] = [];

  editForm = this.fb.group({
    id: [],
    version: ['', Validators.required],
    path: [],
    type: ['', Validators.required],
    releaseDate: [],
    file: ['', Validators.required],
    board: ['', Validators.required],
  });
  private file?: File = undefined;

  constructor(
    protected updateService: UpdateService,
    protected boardUpdateService: BoardUpdateService,
    protected boardService: BoardService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    const boardUpdate = new BoardUpdate();

    // if (boardUpdate.id === undefined) {
    const today = dayjs().startOf('day');
    boardUpdate.releaseDate = today;
    // }

    this.updateForm(boardUpdate);

    this.loadRelationshipsOptions();
  }

  previousState(): void {
    // window.history.back();
  }

  save(): void {
    if (this.file) {
      this.isSaving = true;

      const boardUpdate = this.createFromForm();
      this.updateService
        .uploadFile(this.file, `${boardUpdate.board!.serial!}/${boardUpdate.type!}`, `autoupdate_${boardUpdate.version!}.zip`)
        .pipe(
          switchMap(({ path }) => this.boardUpdateService.create({ ...boardUpdate, path })),
          finalize(() => (this.isSaving = false))
        )
        .subscribe(
          () => this.onSaveSuccess(),
          () => this.onSaveError()
        );
    }
  }

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
  }

  fileChanged($event: Event): void {
    const target = $event.target as HTMLInputElement;
    if (target.files) {
      this.file = target.files[0];
      this.editForm.patchValue({
        file: target.files[0],
      });
    }
  }

  protected onSaveSuccess(): void {
    // this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected updateForm(boardUpdate: IBoardUpdate): void {
    this.editForm.patchValue({
      id: boardUpdate.id,
      version: boardUpdate.version,
      path: boardUpdate.path,
      type: boardUpdate.type,
      releaseDate: boardUpdate.releaseDate ? boardUpdate.releaseDate.format(DATE_TIME_FORMAT) : null,
      board: boardUpdate.board,
    });

    this.boardsSharedCollection = this.boardService.addBoardToCollectionIfMissing(this.boardsSharedCollection, boardUpdate.board);
  }

  protected loadRelationshipsOptions(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .pipe(map((boards: IBoard[]) => this.boardService.addBoardToCollectionIfMissing(boards, this.editForm.get('board')!.value)))
      .subscribe((boards: IBoard[]) => (this.boardsSharedCollection = boards));
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
      board: this.editForm.get(['board'])!.value,
    };
  }
}
