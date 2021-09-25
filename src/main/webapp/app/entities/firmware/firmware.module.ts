import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FirmwareComponent } from './list/firmware.component';
import { FirmwareDetailComponent } from './detail/firmware-detail.component';
import { FirmwareUpdateComponent } from './update/firmware-update.component';
import { FirmwareDeleteDialogComponent } from './delete/firmware-delete-dialog.component';
import { FirmwareRoutingModule } from './route/firmware-routing.module';

@NgModule({
  imports: [SharedModule, FirmwareRoutingModule],
  declarations: [FirmwareComponent, FirmwareDetailComponent, FirmwareUpdateComponent, FirmwareDeleteDialogComponent],
  entryComponents: [FirmwareDeleteDialogComponent],
})
export class FirmwareModule {}
