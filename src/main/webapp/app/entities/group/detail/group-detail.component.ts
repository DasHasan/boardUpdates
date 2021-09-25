import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGroup } from '../group.model';

@Component({
  selector: 'jhi-group-detail',
  templateUrl: './group-detail.component.html',
})
export class GroupDetailComponent implements OnInit {
  group: IGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ group }) => {
      this.group = group;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
