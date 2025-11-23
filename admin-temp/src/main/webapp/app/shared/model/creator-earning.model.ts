import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IMoneyPayout } from 'app/shared/model/money-payout.model';
import { IPurchasedContent } from 'app/shared/model/purchased-content.model';
import { IPurchasedSubscription } from 'app/shared/model/purchased-subscription.model';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';

export interface ICreatorEarning {
  id?: number;
  amount?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  creator?: IUserProfile;
  moneyPayout?: IMoneyPayout | null;
  purchasedContent?: IPurchasedContent | null;
  purchasedSubscription?: IPurchasedSubscription | null;
  purchasedTip?: IPurchasedTip | null;
}

export const defaultValue: Readonly<ICreatorEarning> = {
  isDeleted: false,
};
