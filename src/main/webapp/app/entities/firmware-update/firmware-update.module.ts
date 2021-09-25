import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FirmwareUpdateComponent } from './list/firmware-update.component';
import { FirmwareUpdateDetailComponent } from './detail/firmware-update-detail.component';
import { FirmwareUpdateUpdateComponent } from './update/firmware-update-update.component';
import { FirmwareUpdateDeleteDialogComponent } from './delete/firmware-update-delete-dialog.component';
import { FirmwareUpdateRoutingModule } from './route/firmware-update-routing.module';

@NgModule({
  imports: [SharedModule, FirmwareUpdateRoutingModule],
  declarations: [
    FirmwareUpdateComponent,
    FirmwareUpdateDetailComponent,
    FirmwareUpdateUpdateComponent,
    FirmwareUpdateDeleteDialogComponent,
  ],
  entryComponents: [FirmwareUpdateDeleteDialogComponent],
})
export class FirmwareUpdateModule {}
