export interface ISoftware {
  id?: number;
  version?: string | null;
  path?: string | null;
}

export class Software implements ISoftware {
  constructor(public id?: number, public version?: string | null, public path?: string | null) {}
}

export function getSoftwareIdentifier(software: ISoftware): number | undefined {
  return software.id;
}
