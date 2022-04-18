import {Component, OnInit} from "@angular/core";
import {BoardUpdateUpdateComponent} from "app/entities/board-update/update/board-update-update.component";
import {DownloadUrl} from "app/entities/download-url/download-url.model";
import {UpdatePrecondition} from "app/entities/update-precondition/update-precondition.model";

@Component({
  selector: 'jhi-lwi-board-update-update',
  templateUrl: './lwi-board-update-update.component.html',
})
export class LwiBoardUpdateUpdateComponent extends BoardUpdateUpdateComponent implements OnInit {
  save(): void {
    this.isSaving = true;
    const boardUpdate = this.createFromForm();
    if (boardUpdate.id !== undefined) {
      this.subscribeToSaveResponse(this.boardUpdateService.update(boardUpdate));
    } else {
      this.subscribeToSaveResponse(this.boardUpdateService.create(({
        ...boardUpdate,
        downloadUrl: new DownloadUrl(),
        updatePrecondition: new UpdatePrecondition()
      })));
    }
  }
}
