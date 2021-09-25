import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BoardComponent } from '../list/board.component';
import { BoardDetailComponent } from '../detail/board-detail.component';
import { BoardUpdateComponent } from '../update/board-update.component';
import { BoardRoutingResolveService } from './board-routing-resolve.service';

const boardRoute: Routes = [
  {
    path: '',
    component: BoardComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BoardDetailComponent,
    resolve: {
      board: BoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BoardUpdateComponent,
    resolve: {
      board: BoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BoardUpdateComponent,
    resolve: {
      board: BoardRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(boardRoute)],
  exports: [RouterModule],
})
export class BoardRoutingModule {}
