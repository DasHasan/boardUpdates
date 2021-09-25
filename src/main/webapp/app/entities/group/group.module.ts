import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { GroupComponent } from './list/group.component';
import { GroupDetailComponent } from './detail/group-detail.component';
import { GroupUpdateComponent } from './update/group-update.component';
import { GroupDeleteDialogComponent } from './delete/group-delete-dialog.component';
import { GroupRoutingModule } from './route/group-routing.module';

@NgModule({
  imports: [SharedModule, GroupRoutingModule],
  declarations: [GroupComponent, GroupDetailComponent, GroupUpdateComponent, GroupDeleteDialogComponent],
  entryComponents: [GroupDeleteDialogComponent],
})
export class GroupModule {}
