export interface IUpdateVersionPrecondition {
  id?: number;
  version?: string | null;
}

export class UpdateVersionPrecondition implements IUpdateVersionPrecondition {
  constructor(public id?: number, public version?: string | null) {}
}

export function getUpdateVersionPreconditionIdentifier(updateVersionPrecondition: IUpdateVersionPrecondition): number | undefined {
  return updateVersionPrecondition.id;
}
