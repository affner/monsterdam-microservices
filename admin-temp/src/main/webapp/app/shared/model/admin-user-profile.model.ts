import dayjs from 'dayjs';
import { IAssistanceTicket } from 'app/shared/model/assistance-ticket.model';
import { IAdminAnnouncement } from 'app/shared/model/admin-announcement.model';
import { AdminGender } from 'app/shared/model/enumerations/admin-gender.model';

export interface IAdminUserProfile {
  id?: number;
  fullName?: string;
  emailAddress?: string;
  nickName?: string;
  gender?: keyof typeof AdminGender;
  mobilePhone?: string | null;
  lastLoginDate?: dayjs.Dayjs;
  birthDate?: dayjs.Dayjs;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  assignedTickets?: IAssistanceTicket[] | null;
  announcements?: IAdminAnnouncement[] | null;
}

export const defaultValue: Readonly<IAdminUserProfile> = {
  isDeleted: false,
};
