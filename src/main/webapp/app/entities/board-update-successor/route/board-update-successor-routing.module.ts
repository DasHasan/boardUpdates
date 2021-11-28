import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BoardUpdateSuccessorComponent } from '../list/board-update-successor.component';
import { BoardUpdateSuccessorDetailComponent } from '../detail/board-update-successor-detail.component';
import { BoardUpdateSuccessorUpdateComponent } from '../update/board-update-successor-update.component';
import { BoardUpdateSuccessorRoutingResolveService } from './board-update-successor-routing-resolve.service';

const boardUpdateSuccessorRoute: Routes = [
  {
    path: '',
    component: BoardUpdateSuccessorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BoardUpdateSuccessorDetailComponent,
    resolve: {
      boardUpdateSuccessor: BoardUpdateSuccessorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BoardUpdateSuccessorUpdateComponent,
    resolve: {
      boardUpdateSuccessor: BoardUpdateSuccessorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BoardUpdateSuccessorUpdateComponent,
    resolve: {
      boardUpdateSuccessor: BoardUpdateSuccessorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(boardUpdateSuccessorRoute)],
  exports: [RouterModule],
})
export class BoardUpdateSuccessorRoutingModule {}
