import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FirmwareUpdateComponent } from '../list/firmware-update.component';
import { FirmwareUpdateDetailComponent } from '../detail/firmware-update-detail.component';
import { FirmwareUpdateUpdateComponent } from '../update/firmware-update-update.component';
import { FirmwareUpdateRoutingResolveService } from './firmware-update-routing-resolve.service';

const firmwareUpdateRoute: Routes = [
  {
    path: '',
    component: FirmwareUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FirmwareUpdateDetailComponent,
    resolve: {
      firmwareUpdate: FirmwareUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FirmwareUpdateUpdateComponent,
    resolve: {
      firmwareUpdate: FirmwareUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FirmwareUpdateUpdateComponent,
    resolve: {
      firmwareUpdate: FirmwareUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(firmwareUpdateRoute)],
  exports: [RouterModule],
})
export class FirmwareUpdateRoutingModule {}
