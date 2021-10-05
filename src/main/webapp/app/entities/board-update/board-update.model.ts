import * as dayjs from 'dayjs';
import { IBoard } from 'app/entities/board/board.model';
import { UpdateType } from 'app/entities/enumerations/update-type.model';

export interface IBoardUpdate {
  id?: number;
  version?: string | null;
  path?: string | null;
  type?: UpdateType | null;
  releaseDate?: dayjs.Dayjs | null;
  board?: IBoard | null;
}

export class BoardUpdate implements IBoardUpdate {
  constructor(
    public id?: number,
    public version?: string | null,
    public path?: string | null,
    public type?: UpdateType | null,
    public releaseDate?: dayjs.Dayjs | null,
    public board?: IBoard | null
  ) {}
}

export function getBoardUpdateIdentifier(boardUpdate: IBoardUpdate): number | undefined {
  return boardUpdate.id;
}
