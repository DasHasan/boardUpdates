import { IUpdatePrecondition } from 'app/entities/update-precondition/update-precondition.model';

export interface IUpdateVersionPrecondition {
  id?: number;
  version?: string | null;
  updatePrecondition?: IUpdatePrecondition | null;
}

export class UpdateVersionPrecondition implements IUpdateVersionPrecondition {
  constructor(public id?: number, public version?: string | null, public updatePrecondition?: IUpdatePrecondition | null) {}
}

export function getUpdateVersionPreconditionIdentifier(updateVersionPrecondition: IUpdateVersionPrecondition): number | undefined {
  return updateVersionPrecondition.id;
}
