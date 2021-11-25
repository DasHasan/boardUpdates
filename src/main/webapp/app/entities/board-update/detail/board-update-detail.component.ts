import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoardUpdate } from '../board-update.model';
import { UpdateType } from 'app/entities/enumerations/update-type.model';

@Component({
  selector: 'jhi-board-update-detail',
  templateUrl: './board-update-detail.component.html',
})
export class BoardUpdateDetailComponent implements OnInit {
  boardUpdate: IBoardUpdate | null = null;
  requestBody = {};

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUpdate }) => {
      this.boardUpdate = boardUpdate;
      this.requestBody = {
        serial: this.boardUpdate!.board?.serial,
        firmware: this.boardUpdate!.type === UpdateType.FIRMWARE ? this.boardUpdate!.version : '',
        software: this.boardUpdate!.type === UpdateType.SOFTWARE ? this.boardUpdate!.version : '',
        updateKeys: this.boardUpdate!.updateKeys,
      };
    });
  }

  previousState(): void {
    window.history.back();
  }

  download(): void {
    //
  }
}
