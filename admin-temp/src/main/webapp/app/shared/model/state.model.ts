import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IState {
  id?: number;
  stateName?: string;
  isoCode?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  country?: ICountry;
  blockers?: IUserProfile[] | null;
}

export const defaultValue: Readonly<IState> = {
  isDeleted: false,
};
