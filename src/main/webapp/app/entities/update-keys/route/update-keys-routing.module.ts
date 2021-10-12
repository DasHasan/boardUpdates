import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UpdateKeysComponent } from '../list/update-keys.component';
import { UpdateKeysDetailComponent } from '../detail/update-keys-detail.component';
import { UpdateKeysUpdateComponent } from '../update/update-keys-update.component';
import { UpdateKeysRoutingResolveService } from './update-keys-routing-resolve.service';

const updateKeysRoute: Routes = [
  {
    path: '',
    component: UpdateKeysComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UpdateKeysDetailComponent,
    resolve: {
      updateKeys: UpdateKeysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UpdateKeysUpdateComponent,
    resolve: {
      updateKeys: UpdateKeysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UpdateKeysUpdateComponent,
    resolve: {
      updateKeys: UpdateKeysRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(updateKeysRoute)],
  exports: [RouterModule],
})
export class UpdateKeysRoutingModule {}
