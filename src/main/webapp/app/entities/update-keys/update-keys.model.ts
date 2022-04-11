import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';

export interface IUpdateKeys {
  id?: number;
  key?: string | null;
  updatePrecondition?: IUpdatePrecondition | null;
}

export class UpdateKeys implements IUpdateKeys {
  constructor(public id?: number, public key?: string | null, public updatePrecondition?: IUpdatePrecondition | null) {}
}

export function getUpdateKeysIdentifier(updateKeys: IUpdateKeys): number | undefined {
  return updateKeys.id;
}
