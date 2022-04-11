import {Component, OnInit} from "@angular/core";
import {BoardUpdateUpdateComponent} from "app/entities/board-update/update/board-update-update.component";
import {Observable} from "rxjs";
import {IBoardUpdate} from "app/entities/board-update/board-update.model";
import {HttpResponse} from "@angular/common/http";

@Component({
  selector: 'jhi-lwi-board-update-update',
  templateUrl: './lwi-board-update-update.component.html',
})
export class LwiBoardUpdateUpdateComponent extends BoardUpdateUpdateComponent implements OnInit {
}
