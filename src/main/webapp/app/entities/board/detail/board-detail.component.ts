import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoard } from '../board.model';

@Component({
  selector: 'jhi-board-detail',
  templateUrl: './board-detail.component.html',
})
export class BoardDetailComponent implements OnInit {
  board: IBoard | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ board }) => {
      this.board = board;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
