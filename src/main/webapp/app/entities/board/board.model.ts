export interface IBoard {
  id?: number;
  serial?: string | null;
}

export class Board implements IBoard {
  constructor(public id?: number, public serial?: string | null) {}
}

export function getBoardIdentifier(board: IBoard): number | undefined {
  return board.id;
}
