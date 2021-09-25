export interface IGroup {
  id?: number;
  name?: string | null;
}

export class Group implements IGroup {
  constructor(public id?: number, public name?: string | null) {}
}

export function getGroupIdentifier(group: IGroup): number | undefined {
  return group.id;
}
