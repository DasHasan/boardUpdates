import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUpdateKeys } from '../update-keys.model';

@Component({
  selector: 'jhi-update-keys-detail',
  templateUrl: './update-keys-detail.component.html',
})
export class UpdateKeysDetailComponent implements OnInit {
  updateKeys: IUpdateKeys | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ updateKeys }) => {
      this.updateKeys = updateKeys;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
