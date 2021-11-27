import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DownloadComponent } from '../list/download.component';
import { DownloadDetailComponent } from '../detail/download-detail.component';
import { DownloadUpdateComponent } from '../update/download-update.component';
import { DownloadRoutingResolveService } from './download-routing-resolve.service';

const downloadRoute: Routes = [
  {
    path: '',
    component: DownloadComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DownloadDetailComponent,
    resolve: {
      download: DownloadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DownloadUpdateComponent,
    resolve: {
      download: DownloadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DownloadUpdateComponent,
    resolve: {
      download: DownloadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(downloadRoute)],
  exports: [RouterModule],
})
export class DownloadRoutingModule {}
