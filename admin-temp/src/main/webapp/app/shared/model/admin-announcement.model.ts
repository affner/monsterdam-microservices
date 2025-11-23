import dayjs from 'dayjs';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { IAdminUserProfile } from 'app/shared/model/admin-user-profile.model';
import { AdminAnnouncementType } from 'app/shared/model/enumerations/admin-announcement-type.model';

export interface IAdminAnnouncement {
  id?: number;
  announcementType?: keyof typeof AdminAnnouncementType;
  title?: string;
  content?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  announcerMessage?: IDirectMessage | null;
  admin?: IAdminUserProfile;
}

export const defaultValue: Readonly<IAdminAnnouncement> = {};
