import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DownloadUrlComponent } from '../list/download-url.component';
import { DownloadUrlDetailComponent } from '../detail/download-url-detail.component';
import { DownloadUrlUpdateComponent } from '../update/download-url-update.component';
import { DownloadUrlRoutingResolveService } from './download-url-routing-resolve.service';

const downloadUrlRoute: Routes = [
  {
    path: '',
    component: DownloadUrlComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DownloadUrlDetailComponent,
    resolve: {
      downloadUrl: DownloadUrlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DownloadUrlUpdateComponent,
    resolve: {
      downloadUrl: DownloadUrlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DownloadUrlUpdateComponent,
    resolve: {
      downloadUrl: DownloadUrlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(downloadUrlRoute)],
  exports: [RouterModule],
})
export class DownloadUrlRoutingModule {}
