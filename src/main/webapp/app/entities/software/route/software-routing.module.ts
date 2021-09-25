import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SoftwareComponent } from '../list/software.component';
import { SoftwareDetailComponent } from '../detail/software-detail.component';
import { SoftwareUpdateComponent } from '../update/software-update.component';
import { SoftwareRoutingResolveService } from './software-routing-resolve.service';

const softwareRoute: Routes = [
  {
    path: '',
    component: SoftwareComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoftwareDetailComponent,
    resolve: {
      software: SoftwareRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoftwareUpdateComponent,
    resolve: {
      software: SoftwareRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoftwareUpdateComponent,
    resolve: {
      software: SoftwareRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(softwareRoute)],
  exports: [RouterModule],
})
export class SoftwareRoutingModule {}
