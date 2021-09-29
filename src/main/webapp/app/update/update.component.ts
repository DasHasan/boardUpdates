import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ISoftware } from 'app/entities/software/software.model';
import { finalize, map } from 'rxjs/operators';
import { IBoard } from 'app/entities/board/board.model';
import { SoftwareService } from 'app/entities/software/service/software.service';
import { BoardService } from 'app/entities/board/service/board.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { UpdateService } from 'app/update/update.service';

@Component({
  selector: 'jhi-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss'],
})
export class UpdateComponent implements OnInit {
  editForm = this.fb.group({
    softwareVersion: [],
    firmwareVersion: [],
    board: [],
  });
  isSaving = false;
  boardsSharedCollection: IBoard[] = [];
  softwareFile?: File;
  firmwareFile?: File;

  constructor(
    protected softwareService: SoftwareService,
    protected boardService: BoardService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected http: HttpClient,
    protected updateService: UpdateService
  ) {}

  ngOnInit(): void {
    this.loadBoards();
  }

  save(): void {
    // this.isSaving = true;
    // this.subscribeToSaveResponse(this.updateService.save(this.editForm, this.softwareFile, this.firmwareFile));
    this.updateService.save(this.editForm, this.softwareFile, this.firmwareFile);
  }

  onSuccess(software: ISoftware | null): void {
    // eslint-disable-next-line no-console
    console.log(software);
  }

  onSoftwareFileChange($event: Event): void {
    this.softwareFile = this.getFileFromEvent($event);
  }

  onFirmwareFileChange($event: Event): void {
    this.firmwareFile = this.getFileFromEvent($event);
  }

  previousState(): void {
    window.history.back();
  }

  trackBoardById(index: number, item: IBoard): number {
    return item.id!;
  }

  protected getFileFromEvent($event: Event): File | undefined {
    if ($event.target == null) {
      return;
    }

    const file = ($event.target as HTMLInputElement).files;
    if (!file || !file.length) {
      return;
    }

    return file[0];
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
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
