import { IBoard } from 'app/entities/board/board.model';
import { IFirmware } from 'app/entities/firmware/firmware.model';

export interface IFirmwareUpdate {
  id?: number;
  active?: boolean | null;
  board?: IBoard | null;
  from?: IFirmware | null;
  to?: IFirmware | null;
}

export class FirmwareUpdate implements IFirmwareUpdate {
  constructor(
    public id?: number,
    public active?: boolean | null,
    public board?: IBoard | null,
    public from?: IFirmware | null,
    public to?: IFirmware | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getFirmwareUpdateIdentifier(firmwareUpdate: IFirmwareUpdate): number | undefined {
  return firmwareUpdate.id;
}
