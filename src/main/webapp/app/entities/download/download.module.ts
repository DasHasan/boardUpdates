import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DownloadComponent } from './list/download.component';
import { DownloadDetailComponent } from './detail/download-detail.component';
import { DownloadUpdateComponent } from './update/download-update.component';
import { DownloadDeleteDialogComponent } from './delete/download-delete-dialog.component';
import { DownloadRoutingModule } from './route/download-routing.module';

@NgModule({
  imports: [SharedModule, DownloadRoutingModule],
  declarations: [DownloadComponent, DownloadDetailComponent, DownloadUpdateComponent, DownloadDeleteDialogComponent],
  entryComponents: [DownloadDeleteDialogComponent],
})
export class DownloadModule {}
