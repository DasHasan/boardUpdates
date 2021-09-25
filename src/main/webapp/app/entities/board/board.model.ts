import { ISoftware } from 'app/entities/software/software.model';
import { IFirmware } from 'app/entities/firmware/firmware.model';

export interface IBoard {
  id?: number;
  serial?: string | null;
  software?: ISoftware[] | null;
  firmware?: IFirmware[] | null;
}

export class Board implements IBoard {
  constructor(
    public id?: number,
    public serial?: string | null,
    public software?: ISoftware[] | null,
    public firmware?: IFirmware[] | null
  ) {}
}

export function getBoardIdentifier(board: IBoard): number | undefined {
  return board.id;
}
