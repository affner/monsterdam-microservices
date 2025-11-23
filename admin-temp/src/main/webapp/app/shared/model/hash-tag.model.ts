import dayjs from 'dayjs';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { HashtagType } from 'app/shared/model/enumerations/hashtag-type.model';

export interface IHashTag {
  id?: number;
  tagName?: string;
  hashtagType?: keyof typeof HashtagType;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  posts?: IPostFeed[] | null;
  profiles?: IUserProfile[] | null;
}

export const defaultValue: Readonly<IHashTag> = {
  isDeleted: false,
};
