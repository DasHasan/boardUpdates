import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoardUpdateSuccessor } from '../board-update-successor.model';

@Component({
  selector: 'jhi-board-update-successor-detail',
  templateUrl: './board-update-successor-detail.component.html',
})
export class BoardUpdateSuccessorDetailComponent implements OnInit {
  boardUpdateSuccessor: IBoardUpdateSuccessor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ boardUpdateSuccessor }) => {
      this.boardUpdateSuccessor = boardUpdateSuccessor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
