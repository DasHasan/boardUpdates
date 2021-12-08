import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DownloadUrlComponent } from './list/download-url.component';
import { DownloadUrlDetailComponent } from './detail/download-url-detail.component';
import { DownloadUrlUpdateComponent } from './update/download-url-update.component';
import { DownloadUrlDeleteDialogComponent } from './delete/download-url-delete-dialog.component';
import { DownloadUrlRoutingModule } from './route/download-url-routing.module';

@NgModule({
  imports: [SharedModule, DownloadUrlRoutingModule],
  declarations: [DownloadUrlComponent, DownloadUrlDetailComponent, DownloadUrlUpdateComponent, DownloadUrlDeleteDialogComponent],
  entryComponents: [DownloadUrlDeleteDialogComponent],
  exports: [DownloadUrlComponent],
})
export class DownloadUrlModule {}
