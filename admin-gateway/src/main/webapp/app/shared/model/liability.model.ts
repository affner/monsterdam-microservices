import dayjs from 'dayjs';
import { IAccountingRecord } from 'app/shared/model/accounting-record.model';
import { LiabilityType } from 'app/shared/model/enumerations/liability-type.model';

export interface ILiability {
  id?: number;
  name?: string;
  amount?: number;
  dueDate?: dayjs.Dayjs | null;
  type?: keyof typeof LiabilityType;
  accountingRecords?: IAccountingRecord[] | null;
}

export const defaultValue: Readonly<ILiability> = {};
