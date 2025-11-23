import dayjs from 'dayjs';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IPostComment } from 'app/shared/model/post-comment.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IUserMention {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  originPost?: IPostFeed | null;
  originPostComment?: IPostComment | null;
  mentionedUser?: IUserProfile;
}

export const defaultValue: Readonly<IUserMention> = {
  isDeleted: false,
};
