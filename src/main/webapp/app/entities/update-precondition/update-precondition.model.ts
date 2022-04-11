import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { IUpdateKeys } from 'app/entities/update-keys/update-keys.model';
import { IBoard } from 'app/entities/board/board.model';

export interface IUpdatePrecondition {
  id?: number;
  boardUpdate?: IBoardUpdate | null;
  updateKeys?: IUpdateKeys[] | null;
  boards?: IBoard[] | null;
}

export class UpdatePrecondition implements IUpdatePrecondition {
  constructor(
    public id?: number,
    public boardUpdate?: IBoardUpdate | null,
    public updateKeys?: IUpdateKeys[] | null,
    public boards?: IBoard[] | null
  ) {}
}

export function getUpdatePreconditionIdentifier(updatePrecondition: IUpdatePrecondition): number | undefined {
  return updatePrecondition.id;
}
