import dayjs from 'dayjs';
import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';
import { IWalletTransaction } from 'app/shared/model/wallet-transaction.model';
import { ICreatorEarning } from 'app/shared/model/creator-earning.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IContentPackage } from 'app/shared/model/content-package.model';

export interface IPurchasedContent {
  id?: number;
  rating?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  payment?: IPaymentTransaction | null;
  walletTransaction?: IWalletTransaction | null;
  creatorEarning?: ICreatorEarning;
  viewer?: IUserProfile;
  purchasedContentPackage?: IContentPackage;
}

export const defaultValue: Readonly<IPurchasedContent> = {
  isDeleted: false,
};
