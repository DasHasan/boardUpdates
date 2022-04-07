import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {IBoardUpdate} from '../board-update.model';
import {FileService} from 'app/shared/file.service';
import {EventManager} from "app/core/util/event-manager.service";

@Component({
  selector: 'jhi-board-update-detail',
  templateUrl: './board-update-detail.component.html',
})
export class BoardUpdateDetailComponent implements OnInit {
  boardUpdate: IBoardUpdate | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected fileService: FileService,
              protected eventManager: EventManager) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({boardUpdate}) => {
      this.boardUpdate = boardUpdate;
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
