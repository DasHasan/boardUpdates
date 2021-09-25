import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISoftware } from '../software.model';

@Component({
  selector: 'jhi-software-detail',
  templateUrl: './software-detail.component.html',
})
export class SoftwareDetailComponent implements OnInit {
  software: ISoftware | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ software }) => {
      this.software = software;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
