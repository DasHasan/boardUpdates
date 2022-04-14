import { IBoard } from 'app/entities/board/board.model';
import { IUpdateKeys } from 'app/entities/update-keys/update-keys.model';
import { IUpdateVersionPrecondition } from 'app/entities/update-version-precondition/update-version-precondition.model';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

export interface IUpdatePrecondition {
  id?: number;
  status?: string | null;
  boards?: IBoard[] | null;
  updateKeys?: IUpdateKeys[] | null;
  updateVersionPreconditions?: IUpdateVersionPrecondition[] | null;
  boardUpdate?: IBoardUpdate | null;
}

export class UpdatePrecondition implements IUpdatePrecondition {
  constructor(
    public id?: number,
    public status?: string | null,
    public boards?: IBoard[] | null,
    public updateKeys?: IUpdateKeys[] | null,
    public updateVersionPreconditions?: IUpdateVersionPrecondition[] | null,
    public boardUpdate?: IBoardUpdate | null
  ) {}
}

export function getUpdatePreconditionIdentifier(updatePrecondition: IUpdatePrecondition): number | undefined {
  return updatePrecondition.id;
}
