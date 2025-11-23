import dayjs from 'dayjs';
import { ISocialNetwork } from 'app/shared/model/social-network.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IPersonalSocialLinks {
  id?: number;
  thumbnailContentType?: string | null;
  thumbnail?: string | null;
  normalImageContentType?: string | null;
  normalImage?: string | null;
  normalImageS3Key?: string | null;
  thumbnailIconS3Key?: string | null;
  socialLink?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  socialNetwork?: ISocialNetwork | null;
  user?: IUserProfile;
}

export const defaultValue: Readonly<IPersonalSocialLinks> = {
  isDeleted: false,
};
