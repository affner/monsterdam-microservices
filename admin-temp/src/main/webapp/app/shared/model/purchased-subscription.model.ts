import dayjs from 'dayjs';
import { IPaymentTransaction } from 'app/shared/model/payment-transaction.model';
import { IWalletTransaction } from 'app/shared/model/wallet-transaction.model';
import { ICreatorEarning } from 'app/shared/model/creator-earning.model';
import { ISubscriptionBundle } from 'app/shared/model/subscription-bundle.model';
import { IOfferPromotion } from 'app/shared/model/offer-promotion.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { PurchasedSubscriptionStatus } from 'app/shared/model/enumerations/purchased-subscription-status.model';

export interface IPurchasedSubscription {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  endDate?: dayjs.Dayjs;
  subscriptionStatus?: keyof typeof PurchasedSubscriptionStatus;
  viewerId?: number;
  creatorId?: number;
  payment?: IPaymentTransaction | null;
  walletTransaction?: IWalletTransaction | null;
  creatorEarning?: ICreatorEarning;
  subscriptionBundle?: ISubscriptionBundle;
  appliedPromotion?: IOfferPromotion | null;
  viewer?: IUserProfile;
}

export const defaultValue: Readonly<IPurchasedSubscription> = {
  isDeleted: false,
};
