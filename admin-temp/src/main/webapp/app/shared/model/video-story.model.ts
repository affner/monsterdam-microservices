import dayjs from 'dayjs';
import { IUserReport } from 'app/shared/model/user-report.model';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IVideoStory {
  id?: number;
  thumbnailContentType?: string;
  thumbnail?: string;
  thumbnailS3Key?: string;
  contentContentType?: string | null;
  content?: string | null;
  contentS3Key?: string;
  duration?: string | null;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  reports?: IUserReport[] | null;
  messages?: IDirectMessage[] | null;
  creator?: IUserProfile;
}

export const defaultValue: Readonly<IVideoStory> = {
  isDeleted: false,
};
