import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SoftwareComponent } from './list/software.component';
import { SoftwareDetailComponent } from './detail/software-detail.component';
import { SoftwareUpdateComponent } from './update/software-update.component';
import { SoftwareDeleteDialogComponent } from './delete/software-delete-dialog.component';
import { SoftwareRoutingModule } from './route/software-routing.module';

@NgModule({
  imports: [SharedModule, SoftwareRoutingModule],
  declarations: [SoftwareComponent, SoftwareDetailComponent, SoftwareUpdateComponent, SoftwareDeleteDialogComponent],
  entryComponents: [SoftwareDeleteDialogComponent],
})
export class SoftwareModule {}
