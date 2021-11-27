import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDownload } from '../download.model';
import { DownloadService } from '../service/download.service';
import { DownloadDeleteDialogComponent } from '../delete/download-delete-dialog.component';

@Component({
  selector: 'jhi-download',
  templateUrl: './download.component.html',
})
export class DownloadComponent implements OnInit {
  downloads?: IDownload[];
  isLoading = false;

  constructor(protected downloadService: DownloadService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.downloadService.query().subscribe(
      (res: HttpResponse<IDownload[]>) => {
        this.isLoading = false;
        this.downloads = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDownload): number {
    return item.id!;
  }

  delete(download: IDownload): void {
    const modalRef = this.modalService.open(DownloadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.download = download;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
