import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface INotification {
  id?: number;
  readDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  postCommentId?: number | null;
  postFeedId?: number | null;
  directMessageId?: number | null;
  userMentionId?: number | null;
  likeMarkId?: number | null;
  commentedUser?: IUserProfile;
  messagedUser?: IUserProfile;
  mentionerUserInPost?: IUserProfile;
  mentionerUserInComment?: IUserProfile;
}

export const defaultValue: Readonly<INotification> = {
  isDeleted: false,
};
