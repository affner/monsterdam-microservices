import dayjs from 'dayjs';
import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';

export interface IPaymentProvider {
  id?: number;
  providerName?: string;
  description?: string | null;
  apiKeyText?: string;
  apiSecretText?: string;
  endpointText?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  payments?: IPaymentTransaction[] | null;
}

export const defaultValue: Readonly<IPaymentProvider> = {
  isDeleted: false,
};
