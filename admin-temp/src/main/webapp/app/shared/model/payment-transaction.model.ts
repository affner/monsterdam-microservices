import dayjs from 'dayjs';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IPaymentProvider } from 'app/shared/model/payment-provider.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IAccountingRecord } from 'app/shared/model/accounting-record.model';
import { IPurchasedContent } from 'app/shared/model/purchased-content.model';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { IWalletTransaction } from 'app/shared/model/wallet-transaction.model';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';

export interface IPaymentTransaction {
  id?: number;
  amount?: number;
  paymentDate?: dayjs.Dayjs;
  paymentStatus?: keyof typeof GenericStatus;
  paymentReference?: string | null;
  cloudTransactionId?: string | null;
  paymentMethod?: IPaymentMethod | null;
  paymentProvider?: IPaymentProvider | null;
  viewer?: IUserProfile;
  accountingRecord?: IAccountingRecord | null;
  purchasedContent?: IPurchasedContent | null;
  purchasedSubscription?: IPurchasedSubscription | null;
  walletTransaction?: IWalletTransaction | null;
  purchasedTip?: IPurchasedTip | null;
}

export const defaultValue: Readonly<IPaymentTransaction> = {};
