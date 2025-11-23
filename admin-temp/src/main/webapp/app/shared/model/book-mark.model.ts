import dayjs from 'dayjs';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IBookMark {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  post?: IPostFeed;
  message?: IDirectMessage;
  user?: IUserProfile;
}

export const defaultValue: Readonly<IBookMark> = {
  isDeleted: false,
};
