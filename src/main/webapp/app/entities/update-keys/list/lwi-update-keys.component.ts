import {Component, Input, OnInit} from '@angular/core';
import {UpdateKeysComponent} from "app/entities/update-keys/list/update-keys.component";
import {IUpdateKeys} from '../update-keys.model';

@Component({
  selector: 'jhi-lwi-update-keys',
  templateUrl: './lwi-update-keys.component.html',
})
export class LwiUpdateKeysComponent extends UpdateKeysComponent implements OnInit {
  @Input() keys: IUpdateKeys[] | undefined

  ngOnInit(): void {
    super.ngOnInit();
    this.updateKeys = this.keys
  }
}
