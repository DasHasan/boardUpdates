import * as dayjs from 'dayjs';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

export interface IDownload {
  id?: number;
  date?: dayjs.Dayjs | null;
  boardUpdate?: IBoardUpdate | null;
}

export class Download implements IDownload {
  constructor(public id?: number, public date?: dayjs.Dayjs | null, public boardUpdate?: IBoardUpdate | null) {}
}

export function getDownloadIdentifier(download: IDownload): number | undefined {
  return download.id;
}
