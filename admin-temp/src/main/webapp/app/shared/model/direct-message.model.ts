import dayjs from 'dayjs';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { IUserReport } from 'app/shared/model/user-report.model';
import { IVideoStory } from 'app/shared/model/video-story.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IBookMark } from 'app/shared/model/book-mark.model';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { IAdminAnnouncement } from 'app/shared/model/admin-announcement.model';
import { IPurchasedTip } from 'app/shared/model/purchased-tip.model';

export interface IDirectMessage {
  id?: number;
  messageContent?: string;
  readDate?: dayjs.Dayjs | null;
  likeCount?: number | null;
  isHidden?: boolean | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  contentPackage?: IContentPackage | null;
  reports?: IUserReport[] | null;
  responses?: IDirectMessage[] | null;
  responseTo?: IDirectMessage | null;
  repliedStory?: IVideoStory | null;
  user?: IUserProfile;
  bookMarks?: IBookMark[] | null;
  chatRooms?: IChatRoom[] | null;
  adminAnnouncement?: IAdminAnnouncement | null;
  purchasedTip?: IPurchasedTip | null;
}

export const defaultValue: Readonly<IDirectMessage> = {
  isHidden: false,
  isDeleted: false,
};
