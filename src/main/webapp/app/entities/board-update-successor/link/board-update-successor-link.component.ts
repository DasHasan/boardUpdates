import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { IBoardUpdateSuccessor } from 'app/entities/board-update-successor/board-update-successor.model';
import { BoardUpdateSuccessorService } from 'app/entities/board-update-successor/service/board-update-successor.service';
import { filter, map, switchMap } from 'rxjs/operators';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

@Component({
  selector: 'jhi-board-update-successor-link',
  templateUrl: './board-update-successor-link.component.html',
})
export class BoardUpdateSuccessorLinkComponent implements OnInit, OnDestroy {
  @Input() boardUpdateId?: number;
  boardUpdateSuccessor: IBoardUpdateSuccessor | null = null;
  toBoardUpdate: IBoardUpdate | null = null;

  constructor(protected updateSuccessorService: BoardUpdateSuccessorService, protected boardUpdateService: BoardUpdateService) {}

  ngOnDestroy(): void {
    // throw new Error('Method not implemented.');
    this.toBoardUpdate = null;
  }

  ngOnInit(): void {
    // eslint-disable-next-line no-console
    console.log(this.boardUpdateId);
    if (this.boardUpdateId) {
      this.updateSuccessorService
        .query({ 'fromId.equals': this.boardUpdateId })
        .pipe(
          filter(value => !!value.body),
          map(value => value.body![0]),
          filter(value => !!value.to),
          switchMap(value => this.boardUpdateService.find(value.to!.id!)),
          filter(value => !!value.body),
          map(value => value.body)
        )
        .subscribe(value => (this.toBoardUpdate = value), console.error);
    }
  }
}
