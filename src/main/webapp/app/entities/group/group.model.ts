export interface IGroup {
  id?: number;
}

export class Group implements IGroup {
  constructor(public id?: number) {}
}

export function getGroupIdentifier(group: IGroup): number | undefined {
  return group.id;
}
