import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

export interface IBoard {
  id?: number;
  serial?: string | null;
  boardUpdates?: IBoardUpdate[] | null;
}

export class Board implements IBoard {
  constructor(public id?: number, public serial?: string | null, public boardUpdates?: IBoardUpdate[] | null) {}
}

export function getBoardIdentifier(board: IBoard): number | undefined {
  return board.id;
}
