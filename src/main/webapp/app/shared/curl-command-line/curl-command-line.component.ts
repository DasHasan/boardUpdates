import {Component, Input, OnInit} from '@angular/core';
import {UpdateType} from "app/entities/enumerations/update-type.model";
import {BoardUpdate} from "app/entities/board-update/board-update.model";

@Component({
  selector: 'jhi-curl-command-line',
  templateUrl: './curl-command-line.component.html',
  styleUrls: ['./curl-command-line.component.scss']
})
export class CurlCommandLineComponent implements OnInit {
  @Input() boardUpdate?: BoardUpdate;
  currentHost?: string;
  requestBody: any = {};

  ngOnInit(): void {
    this.currentHost = `${window.location.protocol}//${window.location.host}`;
    if (this.boardUpdate?.board) {
      this.requestBody = {
        serial: this.boardUpdate.board.serial,
        version: this.boardUpdate.board.version,
        status: this.boardUpdate.status,
        firmware: (this.boardUpdate.type as UpdateType) === UpdateType.FIRMWARE ? this.boardUpdate.version : '',
        software: (this.boardUpdate.type as UpdateType) === UpdateType.SOFTWARE ? this.boardUpdate.version : '',
        updateKeys: (this.boardUpdate.updateKeys ?? []).map(value => value.key).map(value => `\\"${value as string}\\"`),
      };
    }
  }

}
