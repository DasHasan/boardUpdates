import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoardUpdate } from '../board-update.model';

@Component({
  selector: 'jhi-board-update-detail',
  templateUrl: './board-update-detail.component.html',
})
export class BoardUpdateDetailComponent implements OnInit {
  boardUpdate: IBoardUpdate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUpdate }) => {
      this.boardUpdate = boardUpdate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
