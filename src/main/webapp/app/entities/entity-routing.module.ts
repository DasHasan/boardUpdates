import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'group',
        data: { pageTitle: 'boardUpdatesApp.group.home.title' },
        loadChildren: () => import('./group/group.module').then(m => m.GroupModule),
      },
      {
        path: 'board',
        data: { pageTitle: 'boardUpdatesApp.board.home.title' },
        loadChildren: () => import('./board/board.module').then(m => m.BoardModule),
      },
      {
        path: 'board-update',
        data: { pageTitle: 'boardUpdatesApp.boardUpdate.home.title' },
        loadChildren: () => import('./board-update/board-update.module').then(m => m.BoardUpdateModule),
      },
      {
        path: 'update-keys',
        data: { pageTitle: 'boardUpdatesApp.updateKeys.home.title' },
        loadChildren: () => import('./update-keys/update-keys.module').then(m => m.UpdateKeysModule),
      },
      {
        path: 'board-update-successor',
        data: { pageTitle: 'boardUpdatesApp.boardUpdateSuccessor.home.title' },
        loadChildren: () => import('./board-update-successor/board-update-successor.module').then(m => m.BoardUpdateSuccessorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
