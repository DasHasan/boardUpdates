import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UpdateKeysComponent } from './list/update-keys.component';
import { UpdateKeysDetailComponent } from './detail/update-keys-detail.component';
import { UpdateKeysUpdateComponent } from './update/update-keys-update.component';
import { UpdateKeysDeleteDialogComponent } from './delete/update-keys-delete-dialog.component';
import { UpdateKeysRoutingModule } from './route/update-keys-routing.module';
import {LwiUpdateKeysComponent} from "app/entities/update-keys/list/lwi-update-keys.component";
import {LwiUpdateKeysUpdateComponent} from "app/entities/update-keys/update/lwi-update-keys-update.component";

@NgModule({
  imports: [SharedModule, UpdateKeysRoutingModule],
  declarations: [UpdateKeysComponent, UpdateKeysDetailComponent, UpdateKeysUpdateComponent, UpdateKeysDeleteDialogComponent,
    LwiUpdateKeysComponent,
    LwiUpdateKeysUpdateComponent],
  entryComponents: [UpdateKeysDeleteDialogComponent],
  exports: [
    LwiUpdateKeysComponent
  ]
})
export class UpdateKeysModule {}
