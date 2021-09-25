export interface IFirmware {
  id?: number;
  version?: string | null;
  path?: string | null;
}

export class Firmware implements IFirmware {
  constructor(public id?: number, public version?: string | null, public path?: string | null) {}
}

export function getFirmwareIdentifier(firmware: IFirmware): number | undefined {
  return firmware.id;
}
