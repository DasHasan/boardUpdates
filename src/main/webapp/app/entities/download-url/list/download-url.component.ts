import { Component, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDownloadUrl } from '../download-url.model';
import { DownloadUrlService } from '../service/download-url.service';
import { DownloadUrlDeleteDialogComponent } from '../delete/download-url-delete-dialog.component';

@Component({
  selector: 'jhi-download-url',
  templateUrl: './download-url.component.html',
})
export class DownloadUrlComponent implements OnInit {
  @Input() boardUpdateId?: number;
  downloadUrls?: IDownloadUrl[];
  isLoading = false;

  constructor(protected downloadUrlService: DownloadUrlService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    const query = this.downloadUrlService.query(this.boardUpdateId ? { 'boardUpdateId.equals': this.boardUpdateId } : {});
    query.subscribe(
      (res: HttpResponse<IDownloadUrl[]>) => {
        this.isLoading = false;
        this.downloadUrls = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDownloadUrl): number {
    return item.id!;
  }

  delete(downloadUrl: IDownloadUrl): void {
    const modalRef = this.modalService.open(DownloadUrlDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.downloadUrl = downloadUrl;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
