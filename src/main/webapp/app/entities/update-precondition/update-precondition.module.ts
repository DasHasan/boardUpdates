import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UpdatePreconditionComponent } from './list/update-precondition.component';
import { UpdatePreconditionDetailComponent } from './detail/update-precondition-detail.component';
import { UpdatePreconditionUpdateComponent } from './update/update-precondition-update.component';
import { UpdatePreconditionDeleteDialogComponent } from './delete/update-precondition-delete-dialog.component';
import { UpdatePreconditionRoutingModule } from './route/update-precondition-routing.module';

@NgModule({
  imports: [SharedModule, UpdatePreconditionRoutingModule],
  declarations: [
    UpdatePreconditionComponent,
    UpdatePreconditionDetailComponent,
    UpdatePreconditionUpdateComponent,
    UpdatePreconditionDeleteDialogComponent,
  ],
  entryComponents: [UpdatePreconditionDeleteDialogComponent],
})
export class UpdatePreconditionModule {}
