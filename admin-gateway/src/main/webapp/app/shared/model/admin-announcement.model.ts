import dayjs from 'dayjs';
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
  directMessageId?: number;
  admin?: IAdminUserProfile;
}

export const defaultValue: Readonly<IAdminAnnouncement> = {};
