import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

export interface IBoardUpdateSuccessor {
  id?: number;
  from?: IBoardUpdate | null;
  to?: IBoardUpdate | null;
}

export class BoardUpdateSuccessor implements IBoardUpdateSuccessor {
  constructor(public id?: number, public from?: IBoardUpdate | null, public to?: IBoardUpdate | null) {}
}

export function getBoardUpdateSuccessorIdentifier(boardUpdateSuccessor: IBoardUpdateSuccessor): number | undefined {
  return boardUpdateSuccessor.id;
}
