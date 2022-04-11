import { IBoardUpdate } from 'app/entities/board-update/board-update.model';
import { IUpdateKeys } from 'app/entities/update-keys/update-keys.model';

export interface IUpdatePrecondition {
  id?: number;
  boardUpdate?: IBoardUpdate | null;
  updateKeys?: IUpdateKeys[] | null;
}

export class UpdatePrecondition implements IUpdatePrecondition {
  constructor(public id?: number, public boardUpdate?: IBoardUpdate | null, public updateKeys?: IUpdateKeys[] | null) {}
}

export function getUpdatePreconditionIdentifier(updatePrecondition: IUpdatePrecondition): number | undefined {
  return updatePrecondition.id;
}
