import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UpdateKeysComponent } from './list/update-keys.component';
import { UpdateKeysDetailComponent } from './detail/update-keys-detail.component';
import { UpdateKeysUpdateComponent } from './update/update-keys-update.component';
import { UpdateKeysDeleteDialogComponent } from './delete/update-keys-delete-dialog.component';
import { UpdateKeysRoutingModule } from './route/update-keys-routing.module';

@NgModule({
  imports: [SharedModule, UpdateKeysRoutingModule],
  declarations: [UpdateKeysComponent, UpdateKeysDetailComponent, UpdateKeysUpdateComponent, UpdateKeysDeleteDialogComponent],
  entryComponents: [UpdateKeysDeleteDialogComponent],
})
export class UpdateKeysModule {}
