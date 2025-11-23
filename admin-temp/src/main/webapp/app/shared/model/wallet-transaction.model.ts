import dayjs from 'dayjs';
import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IPurchasedContent } from 'app/shared/model/purchased-content.model';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';
import { WalletTransactionType } from 'app/shared/model/enumerations/wallet-transaction-type.model';

export interface IWalletTransaction {
  id?: number;
  amount?: number;
  lastModifiedDate?: dayjs.Dayjs | null;
  transactionType?: keyof typeof WalletTransactionType;
  createdDate?: dayjs.Dayjs;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  payment?: IPaymentTransaction | null;
  viewer?: IUserProfile;
  purchasedContent?: IPurchasedContent | null;
  purchasedSubscription?: IPurchasedSubscription | null;
  purchasedTip?: IPurchasedTip | null;
}

export const defaultValue: Readonly<IWalletTransaction> = {
  isDeleted: false,
};
