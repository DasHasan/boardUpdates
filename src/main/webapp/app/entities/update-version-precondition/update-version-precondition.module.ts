import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UpdateVersionPreconditionComponent } from './list/update-version-precondition.component';
import { UpdateVersionPreconditionDetailComponent } from './detail/update-version-precondition-detail.component';
import { UpdateVersionPreconditionUpdateComponent } from './update/update-version-precondition-update.component';
import { UpdateVersionPreconditionDeleteDialogComponent } from './delete/update-version-precondition-delete-dialog.component';
import { UpdateVersionPreconditionRoutingModule } from './route/update-version-precondition-routing.module';

@NgModule({
  imports: [SharedModule, UpdateVersionPreconditionRoutingModule],
  declarations: [
    UpdateVersionPreconditionComponent,
    UpdateVersionPreconditionDetailComponent,
    UpdateVersionPreconditionUpdateComponent,
    UpdateVersionPreconditionDeleteDialogComponent,
  ],
  entryComponents: [UpdateVersionPreconditionDeleteDialogComponent],
})
export class UpdateVersionPreconditionModule {}
