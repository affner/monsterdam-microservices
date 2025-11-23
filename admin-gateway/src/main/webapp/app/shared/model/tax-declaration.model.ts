import dayjs from 'dayjs';
import { IAccountingRecord } from 'app/shared/model/accounting-record.model';
import { TaxDeclarationType } from 'app/shared/model/enumerations/tax-declaration-type.model';
import { TaxDeclarationStatus } from 'app/shared/model/enumerations/tax-declaration-status.model';

export interface ITaxDeclaration {
  id?: number;
  year?: number;
  declarationType?: keyof typeof TaxDeclarationType;
  submittedDate?: dayjs.Dayjs | null;
  status?: keyof typeof TaxDeclarationStatus;
  totalIncome?: number | null;
  totalTaxableIncome?: number | null;
  totalTaxPaid?: number | null;
  supportingDocumentsKey?: string | null;
  accountingRecords?: IAccountingRecord[] | null;
}

export const defaultValue: Readonly<ITaxDeclaration> = {};
