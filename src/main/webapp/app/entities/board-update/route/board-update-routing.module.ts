import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BoardUpdateComponent } from '../list/board-update.component';
import { BoardUpdateDetailComponent } from '../detail/board-update-detail.component';
import { BoardUpdateUpdateComponent } from '../update/board-update-update.component';
import { BoardUpdateRoutingResolveService } from './board-update-routing-resolve.service';

const boardUpdateRoute: Routes = [
  {
    path: '',
    component: BoardUpdateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BoardUpdateDetailComponent,
    resolve: {
      boardUpdate: BoardUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BoardUpdateUpdateComponent,
    resolve: {
      boardUpdate: BoardUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BoardUpdateUpdateComponent,
    resolve: {
      boardUpdate: BoardUpdateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(boardUpdateRoute)],
  exports: [RouterModule],
})
export class BoardUpdateRoutingModule {}
