import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UpdateRoutingModule } from './update-routing.module';
import { UpdateComponent } from './update.component';
import { SharedModule } from 'app/shared/shared.module';
import { UpdateListComponent } from './update-list/update-list.component';
import { UpdateEditComponent } from './update-edit/update-edit.component';

@NgModule({
  declarations: [UpdateComponent, UpdateListComponent, UpdateEditComponent],
  imports: [CommonModule, UpdateRoutingModule, SharedModule],
})
export class UpdateModule {}
