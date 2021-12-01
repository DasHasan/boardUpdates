import * as dayjs from 'dayjs';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

export interface IDownloadUrl {
  id?: number;
  expirationDate?: dayjs.Dayjs | null;
  url?: string | null;
  boardUpdate?: IBoardUpdate | null;
}

export class DownloadUrl implements IDownloadUrl {
  constructor(
    public id?: number,
    public expirationDate?: dayjs.Dayjs | null,
    public url?: string | null,
    public boardUpdate?: IBoardUpdate | null
  ) {}
}

export function getDownloadUrlIdentifier(downloadUrl: IDownloadUrl): number | undefined {
  return downloadUrl.id;
}
