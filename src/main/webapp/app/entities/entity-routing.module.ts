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
        path: 'download-url',
        data: { pageTitle: 'boardUpdatesApp.downloadUrl.home.title' },
        loadChildren: () => import('./download-url/download-url.module').then(m => m.DownloadUrlModule),
      },
      {
        path: 'update-precondition',
        data: { pageTitle: 'boardUpdatesApp.updatePrecondition.home.title' },
        loadChildren: () => import('./update-precondition/update-precondition.module').then(m => m.UpdatePreconditionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
