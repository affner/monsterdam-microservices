import dayjs from 'dayjs';
import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';
import { IBudget } from 'app/shared/model/budget.model';
import { IAsset } from 'app/shared/model/asset.model';
import { ILiability } from 'app/shared/model/liability.model';
import { ITaxDeclaration } from 'app/shared/model/tax-declaration.model';
import { IFinancialStatement } from 'app/shared/model/financial-statement.model';
import { AccountingType } from 'app/shared/model/enumerations/accounting-type.model';

export interface IAccountingRecord {
  id?: number;
  date?: dayjs.Dayjs;
  description?: string;
  debit?: number | null;
  credit?: number | null;
  balance?: number;
  accountType?: keyof typeof AccountingType;
  payment?: IPaymentTransaction | null;
  budget?: IBudget | null;
  asset?: IAsset | null;
  liability?: ILiability | null;
  taxDeclarations?: ITaxDeclaration[] | null;
  financialStatements?: IFinancialStatement[] | null;
}

export const defaultValue: Readonly<IAccountingRecord> = {};
