import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDownload } from '../download.model';

@Component({
  selector: 'jhi-download-detail',
  templateUrl: './download-detail.component.html',
})
export class DownloadDetailComponent implements OnInit {
  download: IDownload | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ download }) => {
      this.download = download;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
