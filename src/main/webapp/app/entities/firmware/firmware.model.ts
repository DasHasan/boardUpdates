import { IBoard } from 'app/entities/board/board.model';

export interface IFirmware {
  id?: number;
  version?: string | null;
  path?: string | null;
  board?: IBoard | null;
}

export class Firmware implements IFirmware {
  constructor(public id?: number, public version?: string | null, public path?: string | null, public board?: IBoard | null) {}
}

export function getFirmwareIdentifier(firmware: IFirmware): number | undefined {
  return firmware.id;
}
