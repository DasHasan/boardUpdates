import { IBoard } from 'app/entities/board/board.model';

export interface ISoftware {
  id?: number;
  version?: string | null;
  path?: string | null;
  board?: IBoard | null;
}

export class Software implements ISoftware {
  constructor(public id?: number, public version?: string | null, public path?: string | null, public board?: IBoard | null) {}
}

export function getSoftwareIdentifier(software: ISoftware): number | undefined {
  return software.id;
}
