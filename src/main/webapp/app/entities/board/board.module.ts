import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BoardComponent } from './list/board.component';
import { BoardDetailComponent } from './detail/board-detail.component';
import { BoardUpdateComponent } from './update/board-update.component';
import { BoardDeleteDialogComponent } from './delete/board-delete-dialog.component';
import { BoardRoutingModule } from './route/board-routing.module';

@NgModule({
  imports: [SharedModule, BoardRoutingModule],
  declarations: [BoardComponent, BoardDetailComponent, BoardUpdateComponent, BoardDeleteDialogComponent],
  entryComponents: [BoardDeleteDialogComponent],
})
export class BoardModule {}
