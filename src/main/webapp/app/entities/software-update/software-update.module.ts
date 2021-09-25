import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SoftwareUpdateComponent } from './list/software-update.component';
import { SoftwareUpdateDetailComponent } from './detail/software-update-detail.component';
import { SoftwareUpdateUpdateComponent } from './update/software-update-update.component';
import { SoftwareUpdateDeleteDialogComponent } from './delete/software-update-delete-dialog.component';
import { SoftwareUpdateRoutingModule } from './route/software-update-routing.module';

@NgModule({
  imports: [SharedModule, SoftwareUpdateRoutingModule],
  declarations: [
    SoftwareUpdateComponent,
    SoftwareUpdateDetailComponent,
    SoftwareUpdateUpdateComponent,
    SoftwareUpdateDeleteDialogComponent,
  ],
  entryComponents: [SoftwareUpdateDeleteDialogComponent],
})
export class SoftwareUpdateModule {}
