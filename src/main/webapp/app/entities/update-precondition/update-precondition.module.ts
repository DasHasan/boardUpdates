import {NgModule} from '@angular/core';

import {SharedModule} from 'app/shared/shared.module';
import {UpdatePreconditionComponent} from './list/update-precondition.component';
import {UpdatePreconditionDetailComponent} from './detail/update-precondition-detail.component';
import {UpdatePreconditionUpdateComponent} from './update/update-precondition-update.component';
import {UpdatePreconditionDeleteDialogComponent} from './delete/update-precondition-delete-dialog.component';
import {UpdatePreconditionRoutingModule} from './route/update-precondition-routing.module';
import {
  LwiUpdatePreconditionDetailComponent
} from "app/entities/update-precondition/detail/lwi-update-precondition-detail.component";
import {UpdateKeysModule} from "app/entities/update-keys/update-keys.module";

@NgModule({
  imports: [SharedModule, UpdatePreconditionRoutingModule, UpdateKeysModule],
  declarations: [
    UpdatePreconditionComponent,
    UpdatePreconditionDetailComponent,
    UpdatePreconditionUpdateComponent,
    UpdatePreconditionDeleteDialogComponent,
    LwiUpdatePreconditionDetailComponent
  ],
  entryComponents: [UpdatePreconditionDeleteDialogComponent],
  exports: [
    LwiUpdatePreconditionDetailComponent
  ]
})
export class UpdatePreconditionModule {
}
