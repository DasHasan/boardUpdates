import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FirmwareComponent } from '../list/firmware.component';
import { FirmwareDetailComponent } from '../detail/firmware-detail.component';
import { FirmwareUpdateComponent } from '../update/firmware-update.component';
import { FirmwareRoutingResolveService } from './firmware-routing-resolve.service';

const firmwareRoute: Routes = [
  {
    path: '',
    component: FirmwareComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FirmwareDetailComponent,
    resolve: {
      firmware: FirmwareRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FirmwareUpdateComponent,
    resolve: {
      firmware: FirmwareRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FirmwareUpdateComponent,
    resolve: {
      firmware: FirmwareRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(firmwareRoute)],
  exports: [RouterModule],
})
export class FirmwareRoutingModule {}
