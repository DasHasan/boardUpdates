import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDownloadUrl } from '../download-url.model';

@Component({
  selector: 'jhi-download-url-detail',
  templateUrl: './download-url-detail.component.html',
})
export class DownloadUrlDetailComponent implements OnInit {
  downloadUrl: IDownloadUrl | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ downloadUrl }) => {
      this.downloadUrl = downloadUrl;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
