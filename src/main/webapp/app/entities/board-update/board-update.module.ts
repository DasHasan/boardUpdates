import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BoardUpdateComponent } from './list/board-update.component';
import { BoardUpdateDetailComponent } from './detail/board-update-detail.component';
import { BoardUpdateUpdateComponent } from './update/board-update-update.component';
import { BoardUpdateDeleteDialogComponent } from './delete/board-update-delete-dialog.component';
import { BoardUpdateRoutingModule } from './route/board-update-routing.module';
import {LwiBoardUpdateComponent} from "app/entities/board-update/list/lwi-board-update.component";
import {LwiBoardUpdateDetailComponent} from "app/entities/board-update/detail/lwi-board-update-detail.component";
import {UpdatePreconditionModule} from "app/entities/update-precondition/update-precondition.module";
import {LwiBoardUpdateUpdateComponent} from "app/entities/board-update/update/lwi-board-update-update.component";

@NgModule({
  imports: [SharedModule, BoardUpdateRoutingModule, UpdatePreconditionModule],
  declarations: [BoardUpdateComponent, BoardUpdateDetailComponent, BoardUpdateUpdateComponent, BoardUpdateDeleteDialogComponent,
    LwiBoardUpdateComponent,
    LwiBoardUpdateDetailComponent,
    LwiBoardUpdateUpdateComponent],
  entryComponents: [BoardUpdateDeleteDialogComponent],
})
export class BoardUpdateModule {}
