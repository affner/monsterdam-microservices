import dayjs from 'dayjs';
import { IAssistanceTicket } from 'app/shared/model/assistance-ticket.model';
import { ReportStatus } from 'app/shared/model/enumerations/report-status.model';
import { ReportCategory } from 'app/shared/model/enumerations/report-category.model';

export interface IUserReport {
  id?: number;
  reportDescription?: string | null;
  status?: keyof typeof ReportStatus;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  reportCategory?: keyof typeof ReportCategory;
  reporterId?: number;
  reportedId?: number;
  multimediaId?: number | null;
  messageId?: number | null;
  postId?: number | null;
  commentId?: number | null;
  ticket?: IAssistanceTicket;
}

export const defaultValue: Readonly<IUserReport> = {
  isDeleted: false,
};
