import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';

export interface IBoard {
  id?: number;
  serial?: string | null;
  version?: string | null;
  updatePrecondition?: IUpdatePrecondition | null;
}

export class Board implements IBoard {
  constructor(
    public id?: number,
    public serial?: string | null,
    public version?: string | null,
    public updatePrecondition?: IUpdatePrecondition | null
  ) {}
}

export function getBoardIdentifier(board: IBoard): number | undefined {
  return board.id;
}
