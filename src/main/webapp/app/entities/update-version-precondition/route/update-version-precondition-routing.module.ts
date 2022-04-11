import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UpdateVersionPreconditionComponent } from '../list/update-version-precondition.component';
import { UpdateVersionPreconditionDetailComponent } from '../detail/update-version-precondition-detail.component';
import { UpdateVersionPreconditionUpdateComponent } from '../update/update-version-precondition-update.component';
import { UpdateVersionPreconditionRoutingResolveService } from './update-version-precondition-routing-resolve.service';

const updateVersionPreconditionRoute: Routes = [
  {
    path: '',
    component: UpdateVersionPreconditionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UpdateVersionPreconditionDetailComponent,
    resolve: {
      updateVersionPrecondition: UpdateVersionPreconditionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UpdateVersionPreconditionUpdateComponent,
    resolve: {
      updateVersionPrecondition: UpdateVersionPreconditionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UpdateVersionPreconditionUpdateComponent,
    resolve: {
      updateVersionPrecondition: UpdateVersionPreconditionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(updateVersionPreconditionRoute)],
  exports: [RouterModule],
})
export class UpdateVersionPreconditionRoutingModule {}
