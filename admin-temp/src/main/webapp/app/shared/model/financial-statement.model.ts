import dayjs from 'dayjs';
import { IAccountingRecord } from 'app/shared/model/accounting-record.model';
import { StatementType } from 'app/shared/model/enumerations/statement-type.model';

export interface IFinancialStatement {
  id?: number;
  statementType?: keyof typeof StatementType;
  periodStartDate?: dayjs.Dayjs;
  periodEndDate?: dayjs.Dayjs;
  createdDate?: dayjs.Dayjs;
  accountingRecords?: IAccountingRecord[] | null;
}

export const defaultValue: Readonly<IFinancialStatement> = {};
