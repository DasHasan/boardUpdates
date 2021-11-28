import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BoardUpdateSuccessorComponent } from './list/board-update-successor.component';
import { BoardUpdateSuccessorDetailComponent } from './detail/board-update-successor-detail.component';
import { BoardUpdateSuccessorUpdateComponent } from './update/board-update-successor-update.component';
import { BoardUpdateSuccessorDeleteDialogComponent } from './delete/board-update-successor-delete-dialog.component';
import { BoardUpdateSuccessorRoutingModule } from './route/board-update-successor-routing.module';
import { BoardUpdateSuccessorLinkComponent } from './link/board-update-successor-link.component';

@NgModule({
  imports: [SharedModule, BoardUpdateSuccessorRoutingModule],
  declarations: [
    BoardUpdateSuccessorComponent,
    BoardUpdateSuccessorDetailComponent,
    BoardUpdateSuccessorUpdateComponent,
    BoardUpdateSuccessorDeleteDialogComponent,
    BoardUpdateSuccessorLinkComponent,
  ],
  entryComponents: [BoardUpdateSuccessorDeleteDialogComponent],
  exports: [BoardUpdateSuccessorLinkComponent, BoardUpdateSuccessorComponent],
})
export class BoardUpdateSuccessorModule {}
