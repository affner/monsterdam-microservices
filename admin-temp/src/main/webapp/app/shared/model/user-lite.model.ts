import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { UserGender } from 'app/shared/model/enumerations/user-gender.model';
import { ContentPreference } from 'app/shared/model/enumerations/content-preference.model';

export interface IUserLite {
  id?: number;
  thumbnailContentType?: string | null;
  thumbnail?: string | null;
  thumbnailS3Key?: string | null;
  birthDate?: dayjs.Dayjs;
  gender?: keyof typeof UserGender;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  nickName?: string;
  fullName?: string;
  contentPreference?: keyof typeof ContentPreference;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IUserLite> = {
  isDeleted: false,
};
