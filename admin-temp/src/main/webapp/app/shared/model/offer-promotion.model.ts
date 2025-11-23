import dayjs from 'dayjs';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { OfferPromotionType } from 'app/shared/model/enumerations/offer-promotion-type.model';

export interface IOfferPromotion {
  id?: number;
  freeDaysDuration?: string | null;
  discountPercentage?: number | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  subscriptionsLimit?: number | null;
  linkCode?: string;
  isFinished?: boolean;
  promotionType?: keyof typeof OfferPromotionType;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  purchasedSubscriptions?: IPurchasedSubscription[] | null;
  creator?: IUserProfile;
}

export const defaultValue: Readonly<IOfferPromotion> = {
  isFinished: false,
  isDeleted: false,
};
