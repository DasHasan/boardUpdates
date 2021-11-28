import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BoardUpdateComponent } from './list/board-update.component';
import { BoardUpdateDetailComponent } from './detail/board-update-detail.component';
import { BoardUpdateUpdateComponent } from './update/board-update-update.component';
import { BoardUpdateDeleteDialogComponent } from './delete/board-update-delete-dialog.component';
import { BoardUpdateRoutingModule } from './route/board-update-routing.module';
import { UpdateKeysModule } from 'app/entities/update-keys/update-keys.module';
import { BoardUpdateSuccessorModule } from 'app/entities/board-update-successor/board-update-successor.module';

@NgModule({
  imports: [SharedModule, BoardUpdateRoutingModule, UpdateKeysModule, BoardUpdateSuccessorModule],
  declarations: [BoardUpdateComponent, BoardUpdateDetailComponent, BoardUpdateUpdateComponent, BoardUpdateDeleteDialogComponent],
  entryComponents: [BoardUpdateDeleteDialogComponent],
  exports: [BoardUpdateComponent],
})
export class BoardUpdateModule {}
