import { IBoard } from 'app/entities/board/board.model';
import { ISoftware } from 'app/entities/software/software.model';

export interface ISoftwareUpdate {
  id?: number;
  active?: boolean | null;
  board?: IBoard | null;
  from?: ISoftware | null;
  to?: ISoftware | null;
}

export class SoftwareUpdate implements ISoftwareUpdate {
  constructor(
    public id?: number,
    public active?: boolean | null,
    public board?: IBoard | null,
    public from?: ISoftware | null,
    public to?: ISoftware | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getSoftwareUpdateIdentifier(softwareUpdate: ISoftwareUpdate): number | undefined {
  return softwareUpdate.id;
}
