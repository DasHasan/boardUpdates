import { IFirmware } from 'app/entities/firmware/firmware.model';
import { ISoftware } from 'app/entities/software/software.model';

export interface IBoard {
  id?: number;
  serial?: string | null;
  firmware?: IFirmware[] | null;
  software?: ISoftware[] | null;
}

export class Board implements IBoard {
  constructor(
    public id?: number,
    public serial?: string | null,
    public firmware?: IFirmware[] | null,
    public software?: ISoftware[] | null
  ) {}
}

export function getBoardIdentifier(board: IBoard): number | undefined {
  return board.id;
}
