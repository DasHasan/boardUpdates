import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SoftwareUpdateComponent } from '../list/software-update.component';
import { SoftwareUpdateDetailComponent } from '../detail/software-update-detail.component';
import { SoftwareUpdateUpdateComponent } from '../update/software-update-update.component';
import { SoftwareUpdateRoutingResolveService } from './software-update-routing-resolve.service';

const softwareUpdateRoute: Routes = [
  {
    path: '',
    component: SoftwareUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoftwareUpdateDetailComponent,
    resolve: {
      softwareUpdate: SoftwareUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoftwareUpdateUpdateComponent,
    resolve: {
      softwareUpdate: SoftwareUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoftwareUpdateUpdateComponent,
    resolve: {
      softwareUpdate: SoftwareUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(softwareUpdateRoute)],
  exports: [RouterModule],
})
export class SoftwareUpdateRoutingModule {}
