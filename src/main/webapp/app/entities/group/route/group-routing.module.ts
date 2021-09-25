import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GroupComponent } from '../list/group.component';
import { GroupDetailComponent } from '../detail/group-detail.component';
import { GroupUpdateComponent } from '../update/group-update.component';
import { GroupRoutingResolveService } from './group-routing-resolve.service';

const groupRoute: Routes = [
  {
    path: '',
    component: GroupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GroupDetailComponent,
    resolve: {
      group: GroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GroupUpdateComponent,
    resolve: {
      group: GroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GroupUpdateComponent,
    resolve: {
      group: GroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(groupRoute)],
  exports: [RouterModule],
})
export class GroupRoutingModule {}
