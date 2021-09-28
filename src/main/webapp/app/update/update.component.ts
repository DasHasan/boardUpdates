import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ISoftware, Software } from 'app/entities/software/software.model';
import { finalize, map } from 'rxjs/operators';
import { IBoard } from 'app/entities/board/board.model';
import { SoftwareService } from 'app/entities/software/service/software.service';
import { BoardService } from 'app/entities/board/service/board.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss'],
})
export class UpdateComponent implements OnInit {
  editForm = this.fb.group({
    id: [],
    version: [],
    path: [],
    file: '',
    board: [],
  });
  isSaving = false;
  boardsSharedCollection: IBoard[] = [];
  file?: File;

  constructor(
    protected softwareService: SoftwareService,
    protected boardService: BoardService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadBoards();
  }

  save(): void {
    this.isSaving = true;
    const software = this.createFromForm();
    // eslint-disable-next-line no-console
    console.log(this.editForm.get('file')?.value);
    this.subscribeToSaveResponse(this.softwareService.create(software));
  }

  onSuccess(software: ISoftware | null): void {
    // eslint-disable-next-line no-console
    console.log(software?.id);
    if (software?.id) {
      if (this.file) {
        const formData = new FormData();
        formData.append('file', this.file);
        this.http.post<{ path: string }>('/api/file-upload/upload-software', formData).subscribe(value => {
          // eslint-disable-next-line no-console
          console.log(value.path);
          software.path = value.path;
          this.softwareService.update(software).subscribe();
        });
      }
    }
  }

  onFileChange($event: Event): void {
    if ($event.target != null) {
      const file = ($event.target as HTMLInputElement).files;
      if (file && file.length > 0) {
        this.file = file[0];
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
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

  protected loadBoards(): void {
    this.boardService
      .query()
      .pipe(map((res: HttpResponse<IBoard[]>) => res.body ?? []))
      .pipe(map((boards: IBoard[]) => this.boardService.addBoardToCollectionIfMissing(boards, this.editForm.get('board')!.value)))
      .subscribe((boards: IBoard[]) => (this.boardsSharedCollection = boards));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoftware>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      software => this.onSuccess(software.body),
      () => this.onSaveError()
    );
  }
}
