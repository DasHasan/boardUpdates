import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

export interface IUpdatePrecondition {
  id?: number;
  boardUpdate?: IBoardUpdate | null;
}

export class UpdatePrecondition implements IUpdatePrecondition {
  constructor(public id?: number, public boardUpdate?: IBoardUpdate | null) {}
}

export function getUpdatePreconditionIdentifier(updatePrecondition: IUpdatePrecondition): number | undefined {
  return updatePrecondition.id;
}
