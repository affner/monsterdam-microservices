import dayjs from 'dayjs';
import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';

export interface IPaymentMethod {
  id?: number;
  methodName?: string;
  tokenText?: string;
  expirationDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  payments?: IPaymentTransaction[] | null;
}

export const defaultValue: Readonly<IPaymentMethod> = {
  isDeleted: false,
};
