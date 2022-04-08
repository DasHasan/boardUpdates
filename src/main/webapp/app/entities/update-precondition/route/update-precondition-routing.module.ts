import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UpdatePreconditionComponent } from '../list/update-precondition.component';
import { UpdatePreconditionDetailComponent } from '../detail/update-precondition-detail.component';
import { UpdatePreconditionUpdateComponent } from '../update/update-precondition-update.component';
import { UpdatePreconditionRoutingResolveService } from './update-precondition-routing-resolve.service';

const updatePreconditionRoute: Routes = [
  {
    path: '',
    component: UpdatePreconditionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UpdatePreconditionDetailComponent,
    resolve: {
      updatePrecondition: UpdatePreconditionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UpdatePreconditionUpdateComponent,
    resolve: {
      updatePrecondition: UpdatePreconditionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UpdatePreconditionUpdateComponent,
    resolve: {
      updatePrecondition: UpdatePreconditionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(updatePreconditionRoute)],
  exports: [RouterModule],
})
export class UpdatePreconditionRoutingModule {}
