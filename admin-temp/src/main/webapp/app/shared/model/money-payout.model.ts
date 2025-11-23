import dayjs from 'dayjs';
import { ICreatorEarning } from 'app/shared/model/creator-earning.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { PayoutStatus } from 'app/shared/model/enumerations/payout-status.model';

export interface IMoneyPayout {
  id?: number;
  amount?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  withdrawStatus?: keyof typeof PayoutStatus;
  creatorEarning?: ICreatorEarning;
  creator?: IUserProfile;
}

export const defaultValue: Readonly<IMoneyPayout> = {
  isDeleted: false,
};
