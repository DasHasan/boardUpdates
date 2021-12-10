import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoardUpdate } from '../board-update.model';
import { UpdateType } from 'app/entities/enumerations/update-type.model';
import { FileService } from 'app/shared/file.service';

@Component({
  selector: 'jhi-board-update-detail',
  templateUrl: './board-update-detail.component.html',
})
export class BoardUpdateDetailComponent implements OnInit {
  boardUpdate: IBoardUpdate | null = null;
  requestBody: any = {};
  currentHost = '';

  constructor(protected activatedRoute: ActivatedRoute, protected fileService: FileService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUpdate }) => {
      this.boardUpdate = boardUpdate;
      this.currentHost = `${window.location.protocol}//${window.location.host}`;
      this.requestBody = {
        serial: this.boardUpdate!.board?.serial,
        firmware: (this.boardUpdate!.type as UpdateType) === UpdateType.FIRMWARE ? this.boardUpdate!.version : '',
        software: (this.boardUpdate!.type as UpdateType) === UpdateType.SOFTWARE ? this.boardUpdate!.version : '',
        updateKeys: (this.boardUpdate!.updateKeys ?? []).map(value => value.key).map(value => `\\"${value as string}\\"`),
      };
    });
  }

  previousState(): void {
    window.history.back();
  }

  download(): void {
    if (this.boardUpdate?.id) {
      this.fileService.download(this.boardUpdate.id);
    }
  }
}
