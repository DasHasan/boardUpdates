import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

export interface IUpdateKeys {
  id?: number;
  key?: string | null;
  boardUpdate?: IBoardUpdate | null;
}

export class UpdateKeys implements IUpdateKeys {
  constructor(public id?: number, public key?: string | null, public boardUpdate?: IBoardUpdate | null) {}
}

export function getUpdateKeysIdentifier(updateKeys: IUpdateKeys): number | undefined {
  return updateKeys.id;
}
