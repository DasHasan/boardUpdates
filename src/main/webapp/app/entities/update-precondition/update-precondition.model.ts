export interface IUpdatePrecondition {
  id?: number;
}

export class UpdatePrecondition implements IUpdatePrecondition {
  constructor(public id?: number) {}
}

export function getUpdatePreconditionIdentifier(updatePrecondition: IUpdatePrecondition): number | undefined {
  return updatePrecondition.id;
}
