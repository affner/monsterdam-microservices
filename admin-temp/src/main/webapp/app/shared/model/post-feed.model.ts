import dayjs from 'dayjs';
import { IPostPoll } from 'app/shared/model/post-poll.model';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { IUserReport } from 'app/shared/model/user-report.model';
import { IPostComment } from 'app/shared/model/post-comment.model';
import { IUserMention } from 'app/shared/model/user-mention.model';
import { IHashTag } from 'app/shared/model/hash-tag.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IBookMark } from 'app/shared/model/book-mark.model';

export interface IPostFeed {
  id?: number;
  postContent?: string;
  isHidden?: boolean | null;
  pinnedPost?: boolean | null;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  poll?: IPostPoll | null;
  contentPackage?: IContentPackage | null;
  reports?: IUserReport[] | null;
  comments?: IPostComment[] | null;
  commentMentions?: IUserMention[] | null;
  hashTags?: IHashTag[] | null;
  creator?: IUserProfile;
  bookMarks?: IBookMark[] | null;
}

export const defaultValue: Readonly<IPostFeed> = {
  isHidden: false,
  pinnedPost: false,
  isDeleted: false,
};
