import dayjs from 'dayjs';
import { IModerationAction } from 'app/shared/model/moderation-action.model';
import { IUserReport } from 'app/shared/model/user-report.model';
import { IIdentityDocumentReview } from 'app/shared/model/identity-document-review.model';
import { IAdminUserProfile } from 'app/shared/model/admin-user-profile.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';
import { TicketType } from 'app/shared/model/enumerations/ticket-type.model';

export interface IAssistanceTicket {
  id?: number;
  subject?: string;
  description?: string;
  status?: keyof typeof TicketStatus;
  type?: keyof typeof TicketType;
  openedAt?: dayjs.Dayjs | null;
  closedAt?: dayjs.Dayjs | null;
  comments?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  moderationAction?: IModerationAction | null;
  report?: IUserReport | null;
  documentsReview?: IIdentityDocumentReview | null;
  assignedAdmin?: IAdminUserProfile | null;
  user?: IUserProfile | null;
}

export const defaultValue: Readonly<IAssistanceTicket> = {};
