import dayjs from 'dayjs';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ISubscriptionBundle {
  id?: number;
  amount?: number;
  duration?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  selledSubscriptions?: IPurchasedSubscription[] | null;
  creator?: IUserProfile;
}

export const defaultValue: Readonly<ISubscriptionBundle> = {
  isDeleted: false,
};
