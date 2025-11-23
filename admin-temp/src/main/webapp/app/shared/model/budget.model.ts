import { IAccountingRecord } from 'app/shared/model/accounting-record.model';

export interface IBudget {
  id?: number;
  year?: number;
  totalBudget?: number;
  spentAmount?: number | null;
  remainingAmount?: number | null;
  budgetDetails?: string | null;
  accountingRecords?: IAccountingRecord[] | null;
}

export const defaultValue: Readonly<IBudget> = {};
