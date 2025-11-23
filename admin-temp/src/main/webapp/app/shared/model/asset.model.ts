import dayjs from 'dayjs';
import { IAccountingRecord } from 'app/shared/model/accounting-record.model';
import { AssetType } from 'app/shared/model/enumerations/asset-type.model';

export interface IAsset {
  id?: number;
  name?: string;
  value?: number;
  acquisitionDate?: dayjs.Dayjs | null;
  type?: keyof typeof AssetType;
  accountingRecords?: IAccountingRecord[] | null;
}

export const defaultValue: Readonly<IAsset> = {};
