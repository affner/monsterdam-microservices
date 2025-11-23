import dayjs from 'dayjs';
import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';
import { IWalletTransaction } from 'app/shared/model/wallet-transaction.model';
import { ICreatorEarning } from 'app/shared/model/creator-earning.model';
import { IDirectMessage } from 'app/shared/model/direct-message.model';

export interface IPurchasedTip {
  id?: number;
  amount?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  payment?: IPaymentTransaction | null;
  walletTransaction?: IWalletTransaction | null;
  creatorEarning?: ICreatorEarning;
  message?: IDirectMessage;
}

export const defaultValue: Readonly<IPurchasedTip> = {
  isDeleted: false,
};
