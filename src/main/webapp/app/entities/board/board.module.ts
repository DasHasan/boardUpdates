import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BoardComponent } from './list/board.component';
import { BoardDetailComponent } from './detail/board-detail.component';
import { BoardUpdateComponent } from './update/board-update.component';
import { BoardDeleteDialogComponent } from './delete/board-delete-dialog.component';
import { BoardRoutingModule } from './route/board-routing.module';
import { BoardUpdateModule } from 'app/entities/board-update/board-update.module';

@NgModule({
  imports: [SharedModule, BoardRoutingModule, BoardUpdateModule],
  declarations: [BoardComponent, BoardDetailComponent, BoardUpdateComponent, BoardDeleteDialogComponent],
  entryComponents: [BoardDeleteDialogComponent],
})
export class BoardModule {}
