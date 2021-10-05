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
        path: 'firmware',
        data: { pageTitle: 'boardUpdatesApp.firmware.home.title' },
        loadChildren: () => import('./firmware/firmware.module').then(m => m.FirmwareModule),
      },
      {
        path: 'firmware-update',
        data: { pageTitle: 'boardUpdatesApp.firmwareUpdate.home.title' },
        loadChildren: () => import('./firmware-update/firmware-update.module').then(m => m.FirmwareUpdateModule),
      },
      {
        path: 'software',
        data: { pageTitle: 'boardUpdatesApp.software.home.title' },
        loadChildren: () => import('./software/software.module').then(m => m.SoftwareModule),
      },
      {
        path: 'software-update',
        data: { pageTitle: 'boardUpdatesApp.softwareUpdate.home.title' },
        loadChildren: () => import('./software-update/software-update.module').then(m => m.SoftwareUpdateModule),
      },
      {
        path: 'board-update',
        data: { pageTitle: 'boardUpdatesApp.boardUpdate.home.title' },
        loadChildren: () => import('./board-update/board-update.module').then(m => m.BoardUpdateModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
