import dayjs from 'dayjs';
import { IUserReport } from 'app/shared/model/user-report.model';
import { IUserMention } from 'app/shared/model/user-mention.model';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IPostComment {
  id?: number;
  commentContent?: string;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  reports?: IUserReport[] | null;
  responses?: IPostComment[] | null;
  commentMentions?: IUserMention[] | null;
  post?: IPostFeed;
  responseTo?: IPostComment | null;
  commenter?: IUserProfile;
}

export const defaultValue: Readonly<IPostComment> = {
  isDeleted: false,
};
